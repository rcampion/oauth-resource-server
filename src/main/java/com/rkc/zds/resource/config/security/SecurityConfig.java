package com.rkc.zds.resource.config.security;

import java.io.InputStream;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	
	/**
	 * Registers the KeycloakAuthenticationProvider with the authentication manager.
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
		grantedAuthorityMapper.setPrefix("ROLE_");
		grantedAuthorityMapper.setConvertToUpperCase(true);
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	@Autowired
	public KeycloakClientRequestFactory keycloakClientRequestFactory;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KeycloakRestTemplate keycloakRestTemplate() {
		return new KeycloakRestTemplate(keycloakClientRequestFactory);
	}

	/**
	 * Defines the session authentication strategy.
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	public KeycloakConfigResolver KeycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .maximumSessions(1);
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/api/uploadFile", "/api/uploadResume", "/api/sso/getuser", "/api/dashboard*",
				"/api/dashboard/**", "/api/dashboard/email**", "/api/dashboard/email/**",
				"/api/dashboard/email/send", "/api/dashboard/email/send/**", "/api/user/registration",
				"/live**", "/live/**", "/live/info*")
		.permitAll();
		
		http.csrf()
		// ignore our stomp endpoints since they are protected using Stomp headers
		.ignoringAntMatchers("/live/**").and().headers()
		// allow same origin to frame our site to support iframe SockJS
		.frameOptions().sameOrigin().and().authorizeRequests();

		http.cors().and().authorizeRequests()
		.antMatchers(HttpMethod.GET, "api/user/contacts*","api/user/contacts/friends/*","api/user/contacts/friends*", "data/files/uploads/*","/api/users/active", "/api/sso/logout", "/api/log*", "/api/log/**", "/api/log/pageviews*",
				"/api/errorlog*", "/api/errorlog/**", "/api/articles*", "/api/tags*", "/live**", "/live/**",
				"/live/info*", "/api/dashboard/whois*", "/api/dashboard/whois/**")
		.permitAll()
		.antMatchers(HttpMethod.POST, "/api/uploadFile", "/api/uploadResume", "/api/sso/getuser", "/api/dashboard*",
				"/api/dashboard/**", "/api/dashboard/email**", "/api/dashboard/email/**",
				"/api/dashboard/email/send", "/api/dashboard/email/send/**", "/api/user/registration",
				"/live**", "/live/**", "/live/info*")
		.permitAll()

		.and().oauth2ResourceServer().jwt();
        
		http.csrf().disable(); // ADD THIS CODE TO DISABLE CSRF IN PROJECT.**

	}

	/**
	 * Overrides default keycloak config resolver behaviour (/WEB-INF/keycloak.json)
	 * by a simple mechanism.
	 * <p>
	 * This example loads other-keycloak.json when the parameter use.other is set to
	 * true, e.g.: {@code ./gradlew bootRun -Duse.other=true}
	 *
	 * @return keycloak config resolver
	 */
	@Bean
	public KeycloakConfigResolver keycloakConfigResolver() {
		return new KeycloakConfigResolver() {

			private KeycloakDeployment keycloakDeployment;

			@Override
			public KeycloakDeployment resolve(HttpFacade.Request facade) {
				if (keycloakDeployment != null) {
					return keycloakDeployment;
				}

				String path = "/keycloak.json";
				InputStream configInputStream = getClass().getResourceAsStream(path);

				if (configInputStream == null) {
					throw new RuntimeException("Could not load Keycloak deployment info: " + path);
				} else {
					keycloakDeployment = KeycloakDeploymentBuilder.build(configInputStream);
				}

				return keycloakDeployment;
			}
		};
	}
}

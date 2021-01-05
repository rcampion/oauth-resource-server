package com.rkc.zds.resource.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The persistent class for the PCM_USERS database table.
 * 
 */
@Entity
@Table(name="PCM_USERS")
public class UserDto implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
    public UserDto() {
    }
    
	public UserDto(String userName, String password, int enabled) {
		this.userName = userName;
		this.password = password;
		this.enabled = enabled;
	}

	@Column(name="CONTACT_ID")
	private Integer contactId;

	@Column(name="LOGIN")
    private String login;
	
	@Column(name="USERNAME")
	private String userName;

	@Column(name="ENABLED")	
	private int enabled;

	@Column(name="PASSWORD")	
	private String password;
	
	@Column(name="FIRSTNAME")	
	private String firstName;
	
	@Column(name="LASTNAME")	
	private String lastName;
	
	@Column(name="EMAIL")	
    private String email;
	
	@Column(name="BIO")		
	private String bio;
	
	@Column(name="IMAGE")	
    private String image;
	
	@Column(name="ISLOGGEDIN")	
    private int isLoggedIn;	
	
	@OneToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable=true, name = "userName", referencedColumnName = "userName")
	@ElementCollection(targetClass=AuthorityDto.class)

    @JsonProperty("authorities")
	//private Set<AuthorityDto> authorities = new HashSet<AuthorityDto>(0);
    private List<AuthorityDto> authorities = new ArrayList<AuthorityDto>();

    @JsonIgnore
	@Column(name="PUBLIC_SECRET")
    private String publicSecret;

    @JsonIgnore
	@Column(name="PRIVATE_SECRET")
    private String privateSecret;

    private Profile profile;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
    public int getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(int isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
/*	
	public Set<AuthorityDto> getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(Set<AuthorityDto> authorityDto) {
		this.authorities= authorityDto;
	}
*/
    @JsonIgnore
    public List<AuthorityDto> getAuthorities() {
		return authorities;
	}
    
    @JsonIgnore
	public void setAuthorities(List<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

	public boolean isEnabled() {
		if(this.enabled == 1)
			return true;
		return false;
	}
	
	public String getPublicSecret() {
		return publicSecret;
	}

	public void setPublicSecret(String publicSecret) {
		this.publicSecret = publicSecret;
	}

	public String getPrivateSecret() {
		return privateSecret;
	}

	public void setPrivateSecret(String privateSecret) {
		this.privateSecret = privateSecret;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
/*
    public List<String> getAuthoritiesList() {
    	
    	List<String> authoritiesList = new ArrayList<String>();
    	
		Set<AuthorityDto> roles = getAuthorities(); 
		
		for (Iterator<AuthorityDto> iterator = roles.iterator(); iterator.hasNext();) {
			AuthorityDto authority = iterator.next();
			authoritiesList.add(authority.toString());
		}		
		return authoritiesList;
	}

	public void setAuthoritiesList(List<String> authorities) {
		
		Set<AuthorityDto> roles = getAuthorities();
		
		AuthorityDto authority = null;
		
		for (Iterator<AuthorityDto> iterator = roles.iterator(); iterator.hasNext();) {
			authority = iterator.next();
		}	
		
		Set<AuthorityDto> set = new HashSet<AuthorityDto>();
	
		for(String element:authorities) {
			AuthorityDto temp = new AuthorityDto();
			if(authority!=null) temp.setId(authority.getId());
			temp.setAuthority(element);
			temp.setUserName(getUserName());
			set.add(temp);
		}
		
		this.authorities = set;
	}
*/

	public void update(String email, String userName, String password, String bio, String image) {
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.bio = bio;
		this.image = image;
	}
	
/*
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.id);
	    return hash;
	}

	@Override
	public boolean equals(Object other) {
	    boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        UserDto otherUser = (UserDto)other;
	        result = (id == (otherUser.id));
	    } // end else

	    return result;
	}
*/
}
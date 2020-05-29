package com.rkc.zds.resource.dto;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The persistent class for the PCM_AUTHORITIES database table.
 * 
 */
@Entity
@Table(name = "PCM_AUTHORITIES", catalog = "pcm", uniqueConstraints = @UniqueConstraint(columnNames = { "AUTHORITY", "USERNAME" }))
public class AuthorityDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonProperty("id")
	private Integer id;
	
	@ManyToOne(optional=true, fetch = FetchType.EAGER)	
	@JsonIgnore
	private UserDto user;

	private UserDto getUser() {
		return this.user;
	}
	
    private void setUser(UserDto userDto) {
        this.user = userDto;
    }
    
	@Column(name="USERNAME")
	@JoinColumn(nullable=true, name = "USERNAME")
	@JsonProperty("userName")
	private String userName;

	@Column(name = "AUTHORITY", length = 45)
	@JsonProperty("authority")
	private String authority;

    public AuthorityDto() {
    }
    
    public AuthorityDto(String jsonString) {
    	System.out.println(jsonString);
    	StringBuilder sb = new StringBuilder(jsonString);
    	sb.deleteCharAt(0);
    	int len = jsonString.length();
    	sb.deleteCharAt(len-2);
    	String result = sb.toString();
		ObjectMapper mapper = new ObjectMapper();
		AuthorityDto authority = new AuthorityDto();
		try {
			authority = mapper.readValue(result, AuthorityDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.id = authority.id;
		this.userName = authority.userName;
		this.authority = authority.authority;
		
    }
    
	public AuthorityDto(String id, String userName, String authority) {
		this.id = Integer.parseInt(id);
		this.userName = userName;
		this.authority = authority;
	}
/*	
	public AuthorityDto(UserDto user, String role) {
		this.user = user;
		this.authority = role;
	}
*/	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
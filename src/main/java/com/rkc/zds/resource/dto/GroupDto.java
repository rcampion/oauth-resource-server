package com.rkc.zds.resource.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the PCM_GROUP database table.
 * 
 */
@Entity
@Table(name="PCM_GROUP")
public class GroupDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GROUP_ID", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int groupId;
	@Column(name="GROUP_NAME", nullable = false, length = 100)	
	private String groupName;
	@Column(name="GROUP_DESCRIPTION", nullable = false, length = 100)
	private String groupDescription;

    public GroupDto() {
    }
    
	public GroupDto(int groupId, String groupName, String groupDescription) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
	}
	
	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getGroupDescription() {
		return this.groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.groupId);
	    return hash;
	}

	@Override
	public boolean equals(Object other) {
	    boolean result;
	    if((other == null) || (getClass() != other.getClass())){
	        result = false;
	    } // end if
	    else{
	        GroupDto otherGroup = (GroupDto)other;
	        result = (groupId == (otherGroup.groupId));
	    } // end else

	    return result;
	}
}
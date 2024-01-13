package com.omsai.securitydemo.dao;


import com.omsai.securitydemo.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}

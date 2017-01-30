package ir.onsight.entity;

import ir.onsight.dao.UserDao;

import java.sql.SQLException;
import java.util.*;

public class User {
	private String username;
	private String name ;
	private String family ;
	private List<String> roles; 

	public User(String username,String name ,String family,List<String> roles) {
		this.username = username;
		this.name = name;
		this.family = family;
		this.roles = roles;
	}
	
	public User(String username,String name ,String family) {
		this(username, name, family, null);
	}
	
	public User(String username){
		this.username = username;
	}

	public void loadRoles() throws SQLException {
		 roles = UserDao.findRolesByUsername(username);
	}
	
	public void addRole(String role){
		if(roles == null)
			roles = new ArrayList<String>();
		roles.add(role);
	}

	public void clearRoles(){
		if(roles !=null)
			roles.clear();
	}
	
	public List<String> getRoles(){
		return roles;
	}
	
	public void setRoles(List<String> roles){
		this.roles = roles;
	}	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName(){
		return name ;
	}

	public String getFamily(){
		return family ;
	}
}


package ir.onsight.dao;

import ir.onsight.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserDao {
	private static final String CONN_STR = "jdbc:mysql://localhost:3306/onsight?user=onsight_access&password=onsightpass";
	static {
		try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load MySQL JDBC driver");
		}
	}

	public enum UserAuthResult{
		SUCCESSFUL(true),
		WRONG_USER(false),
		WRONG_PASS(false),
		NOT_CONFIRMED(false);

		private boolean result;

		UserAuthResult(boolean result) {
			this.result = result;
		}

		public boolean getResult(){
			return result;
		}

	}

	public static UserAuthResult authenticate(String username,String password) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT is_confirmed,password FROM user WHERE username=? LIMIT 1");
		preStmt.setString(1, username);
		ResultSet userInfo = preStmt.executeQuery();
		UserAuthResult result;
		if(!userInfo.next())
			result = UserAuthResult.WRONG_USER;
		else if(!userInfo.getString("password").equals(password))
			result = UserAuthResult.WRONG_PASS;
		else if(!userInfo.getBoolean("is_confirmed"))
			result = UserAuthResult.NOT_CONFIRMED;
		else
			result = UserAuthResult.SUCCESSFUL;
		con.close();
		return result;
	}

	public static User findUserByUsername(String username) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT name,family FROM user WHERE username=? LIMIT 1");
		preStmt.setString(1, username);
		ResultSet userInfo = preStmt.executeQuery();
		if(!userInfo.next()){
			con.close();
			return null;
		}
		String name = userInfo.getString("name");
		String family = userInfo.getString("family");
		con.close();
		return new User(username, name, family);
	}

	public static boolean isUserExist(String username) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT username FROM user WHERE username=? LIMIT 1");
		preStmt.setString(1, username);
		ResultSet userInfo = preStmt.executeQuery();
		if(!userInfo.next()){
			con.close();
			return false;
		}
		con.close();
		return true;
	}

	public static List<String> findRolesByUsername(String username) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT * FROM user_role WHERE username=?");
		preStmt.setString(1, username);
		ResultSet userRoles = preStmt.executeQuery();
		List<String> roles = new ArrayList<String>();
		while(userRoles.next()){
			roles.add(userRoles.getString("role"));
		}
		con.close();
		return roles;
	}

	public static void insertNewUser(String username,String password,String name, String family,boolean isConfirmed) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("INSERT INTO user(username,password,name,family,is_confirmed) VALUES(?,?,?,?,?)");
		preStmt.setString(1, username);
		preStmt.setString(2, password);
		preStmt.setString(3, name);
		preStmt.setString(4, family);
		preStmt.setBoolean(5, isConfirmed);
		preStmt.executeUpdate();
		con.close();
	}

	public static boolean deleteUser(String username) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("DELETE FROM user_role WHERE username=?");
		preStmt.setString(1, username);
		preStmt.executeUpdate();
		preStmt = con.prepareStatement("DELETE FROM user WHERE username=?");
		preStmt.setString(1, username);
		int res = preStmt.executeUpdate();
		con.close();
		return res==0 ? false : true ;
	}

	public static List<User> findUnconfirmedUsers() throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT * FROM user WHERE is_confirmed=0");
		ResultSet usersInfo = preStmt.executeQuery();
		List<User> unconfirmedUsers = new LinkedList<User>();
		while(usersInfo.next()){
			String name = usersInfo.getString("name");
			String family = usersInfo.getString("family");
			String username = usersInfo.getString("username");
			unconfirmedUsers.add(new User(username, name, family));
		}
		con.close();
		return unconfirmedUsers;
	}

	public static void confirmUser(String username,List<String> roles) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("UPDATE user SET is_confirmed=1 WHERE username=?");
		preStmt.setString(1, username);
		preStmt.executeUpdate();
		if(roles==null)
			return;
		preStmt = con.prepareStatement("INSERT INTO user_role(username,role) VALUES(?,?)");
		preStmt.setString(1, username);
		for(String currRole : roles){
			preStmt.setString(2,currRole);
			preStmt.executeUpdate();
		}
		con.close();
	}

	public static void confirmUser(String username) throws SQLException{
		confirmUser(username,new ArrayList<String>());
	}

	public static void addUserRoles(String username,List<String> roles) throws SQLException{
		if(roles==null)
			return;
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("INSERT INTO user_role(username,role) VALUES(?,?)");
		preStmt.setString(1, username);
		for(String currRole : roles){
			preStmt.setString(2,currRole);
			preStmt.executeUpdate();
		}
		con.close();
	}


}

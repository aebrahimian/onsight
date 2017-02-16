package ir.onsight.dao;

import ir.onsight.entity.Account;
import ir.onsight.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {
	private static final String CONN_STR = "jdbc:mysql://localhost:3306/onsight?user=onsight_access&password=onsightpass";
	static {
		try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load MySQL JDBC driver");
		}
	}
	
	public static Account findAccountByUsername(String username) throws SQLException{	
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT * FROM account WHERE username=? LIMIT 1"); 
		preStmt.setString(1, username);
		ResultSet userInfo = preStmt.executeQuery();
		if(!userInfo.next()){
			con.close();
			return null;
		}
		String password = userInfo.getString("password");
		con.close();
		return new Account(username, password);
	}
	
	public static void insertNewAccount(String username,String password) throws SQLException{	
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("INSERT INTO account(username,password) VALUES(?,?)"); 
		preStmt.setString(1, username);
		preStmt.setString(2, password);
		preStmt.executeUpdate();
		con.close();
	}
}

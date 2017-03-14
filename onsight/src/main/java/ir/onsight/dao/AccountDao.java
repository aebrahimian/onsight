package ir.onsight.dao;

import ir.onsight.entity.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
		ResultSet accountInfo = preStmt.executeQuery();
		if(!accountInfo.next()){
			con.close();
			return null;
		}
		String password = accountInfo.getString("password");
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
	
	public static List<Account> getAllAccounts() throws SQLException{	
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT username FROM account"); 
		ResultSet accountsInfo = preStmt.executeQuery();
		List<Account> accounts = new LinkedList<Account>();
		while(accountsInfo.next()){
			String username = accountsInfo.getString("username");
			accounts.add(new Account(username));
		}
		con.close();
		return accounts;
	}
}

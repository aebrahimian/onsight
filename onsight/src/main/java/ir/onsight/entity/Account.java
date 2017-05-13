package ir.onsight.entity;

import ir.onsight.dao.AccountDao;
import ir.onsight.services.instagram.InstaAPI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

//social media account
public class Account {
	private String username;
	private transient String password;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Account(String username){
		this(username, null);
	}


	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void loadPassword() throws SQLException {
		if (this.password == null)
			this.password = AccountDao.findAccountByUsername(this.username).getPassword();
	}

	public enum AccountAuthResult{
		SUCCESSFUL(true),
		WRONG_USER_PASS(false),
		ACCOUNT_PROBLEM(false),
		NET_ERROR(false);

		private boolean result;

		AccountAuthResult(boolean result) {
			this.result = result;
		}

		public boolean getResult(){
			return result;
		}

	}

	public AccountAuthResult authenticate() throws IOException{
		return InstaAPI.authenticate(this.username, this.password);
	}



}

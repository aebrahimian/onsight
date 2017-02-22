package ir.onsight.entity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonElement;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
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
		URL obj = new URL("https://www.instagram.com/accounts/login/ajax/");
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("cookie","csrftoken=a");
		con.setRequestProperty("x-csrftoken","a");
		con.setRequestProperty("referer", "https://www.instagram.com/");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("username=" + this.username + "&password=" + this.password);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		if(responseCode != 200)
			return AccountAuthResult.NET_ERROR;
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));			
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		String jsonResp = response.toString();
		JsonPrimitive authRes = new JsonParser().parse(jsonResp).getAsJsonObject().getAsJsonPrimitive("authenticated");
		if(authRes.isJsonNull())
			return AccountAuthResult.ACCOUNT_PROBLEM;
		else if(authRes.getAsBoolean())
			return AccountAuthResult.SUCCESSFUL;
		else 
			return AccountAuthResult.WRONG_USER_PASS;
	}
	
	
}

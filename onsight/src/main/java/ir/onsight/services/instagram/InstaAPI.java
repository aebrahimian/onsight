package ir.onsight.services.instagram;

import ir.onsight.entity.Post;
import ir.onsight.entity.Account.AccountAuthResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class InstaAPI {
	public static Boolean uploadPost(Post post){
		try {
			File phpAPIDirectory = new File(InstaAPI.class.getResource("instagram-api-php").toURI());
			String uploadScriptFileName = "upload_post.php";
			String[] command = new String[]{"php",
											uploadScriptFileName,
											post.getAccount().getUsername(),
											post.getAccount().getPassword(),
											post.getMediaAbsolutePath(),
											post.getCaption()};
			Process process = Runtime.getRuntime().exec(command, null, phpAPIDirectory);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = in.readLine()) != null)
				System.out.println(line);
			process.waitFor();
			int exitStatus = process.exitValue();
			if(exitStatus == 1)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static AccountAuthResult authenticate(String username, String password) throws IOException{
		URL obj = new URL("https://www.instagram.com/accounts/login/ajax/");
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("cookie","csrftoken=a");
		con.setRequestProperty("x-csrftoken","a");
		con.setRequestProperty("referer", "https://www.instagram.com/");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("username=" + username + "&password=" + password);
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

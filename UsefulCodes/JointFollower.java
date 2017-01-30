import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

import javax.net.ssl.HttpsURLConnection;

public class JointFollower {
	private static final String CONN_STR = "jdbc:mysql://localhost/instaonsight";
	private static String cookie = "mid=V2sNwAAEAAER2tXAANFTzLJ-87Dv; sessionid=IGSCbdb352a6ab38b7429e59d2ec2c2ce1cc72d17a81444745a115cc59272d6ecf39%3A1OWPVQnyx8rdxg0BVQqGw51iBZW89qOX%3A%7B%22_token_ver%22%3A2%2C%22_auth_user_id%22%3A1525108002%2C%22_token%22%3A%221525108002%3AEMBL3QC1OxHlXwZp21WveIA0vwuCGQ8f%3A9e282997ac74a79f3a9b1b20d8d746ca2232ec23c681da518667bd3ed050695c%22%2C%22asns%22%3A%7B%225.116.200.252%22%3A44244%2C%22time%22%3A1474103688%7D%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22last_refreshed%22%3A1474103689.566004%2C%22_platform%22%3A4%2C%22_auth_user_hash%22%3A%22%22%7D; ig_pr=1; ig_vw=1440; s_network=; csrftoken=ovNWm45somLb09W1rcpK2O7vSlseMhRq; ds_user_id=1525108002";
	private static String csrf = "ovNWm45somLb09W1rcpK2O7vSlseMhRq";
	private static String queryUrl = "https://www.instagram.com/query/";
	private static int transferCount = 2000 ;

	static{
		try {
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	    } catch (Exception ex) {
	        System.out.println("error while load sql jdbc driver .class path problem");
	    }
	}



	public static HttpsURLConnection sendQuery(String urlParameters) throws Exception{
		URL obj = new URL(queryUrl);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("cookie",cookie);
		con.setRequestProperty("x-csrftoken",csrf);
		con.setRequestProperty("referer", "https://www.instagram.com/big.on.sight/");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		return con;
	}

	public static List<String> parseFollowerList(String json){
		List<String> followers = new ArrayList<String>();
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();
		JsonArray nodes = data.getAsJsonObject("followed_by").getAsJsonArray("nodes");
		for(JsonElement currNode : nodes){
			followers.add(currNode.getAsJsonObject().get("username").getAsString());
		}
		return followers;
	}

	public static List<String> getPageFollowers(String username,String userId) throws Exception{
		List<String> followers = new ArrayList<String>();
		boolean isFirst = true ;
		String startCursor = "" ;
		String inputLine = "";
		StringBuffer response = new StringBuffer();
		HttpsURLConnection con;
		
		while(true){
			String urlParameters = "q=ig_user(" + userId + ")+%7B%0A++followed_by." ;
			if(isFirst){
				urlParameters+="first(" + transferCount + ")" ;
				isFirst = false ;
			}
			else{
				urlParameters+="after(" + startCursor + "%2C+" + transferCount +")" ;
			}
			urlParameters+="+%7B%0A++++count%2C%0A++++page_info+%7B%0A++++++end_cursor%2C%0A++++++has_next_page%0A++++%7D%2C%0A++++nodes+%7B%0A++++++id%2C%0A++++++is_verified%2C%0A++++++followed_by_viewer%2C%0A++++++requested_by_viewer%2C%0A++++++full_name%2C%0A++++++profile_pic_url%2C%0A++++++username%0A++++%7D%0A++%7D%0A%7D%0A&ref=relationships%3A%3Afollow_list";
			// Send post request
			while(true){
				System.out.println("getting followers of /page= " + username + "/count= " + transferCount + "/start_cursor= " + startCursor);
				try{
					con = sendQuery(urlParameters);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("error in connection . retry");
					continue;
				}
				int responseCode = con.getResponseCode();
				String responseMessage = con.getResponseMessage();			
				System.out.println("response code = " + responseCode + " response message = " + responseMessage);
				if(responseCode == 200)
					break;				
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));			
			response.setLength(0);
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			String jsonResp = response.toString();
			List<String> followersPart = parseFollowerList(jsonResp);
			followers.addAll(followersPart);
			JsonElement endCursor = new JsonParser().parse(jsonResp).getAsJsonObject().getAsJsonObject("followed_by").getAsJsonObject("page_info").get("end_cursor");
			if(endCursor.isJsonNull())
				break;
			else
				startCursor = endCursor.getAsString();
		}
		return followers;
	}

	public static void insertPageFollowers(String pageUsername,List<String> followerList) throws Exception{
		Connection conn  = DriverManager.getConnection(CONN_STR,"root","");		
		conn.setAutoCommit(false);
		Statement st  = conn.createStatement();
		st.executeUpdate("DELETE FROM FOLLOWER WHERE page_username='" + pageUsername + "'");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO FOLLOWER(page_username,follower_username) VALUES(?,?)");
		for(String currFollower : followerList){
			ps.setString(1,pageUsername);
			ps.setString(2,currFollower);
			ps.addBatch();
		}
		ps.executeBatch();
		conn.commit();
	}

	public static int getJointFollowerCount(String pageUsername1,String pageUsername2) throws Exception{
		Connection conn  = DriverManager.getConnection(CONN_STR,"root","");		
		Statement st  = conn.createStatement();
		ResultSet rs = st.executeQuery("select count(follower_username) as count from follower where page_username = '" + pageUsername1 +
										"' and follower.follower_username IN (select follower_username from follower  where page_username='" + pageUsername2 + "')");
		if(!rs.next()){
			return -1;
		}
		return rs.getInt("count");
	}

	public static int getJointFollowerCount(List<String> usernameList) throws Exception{
		Connection conn  = DriverManager.getConnection(CONN_STR,"root","");		
		Statement st  = conn.createStatement();
		String query = "select count(follower_username) as count from follower where page_username = '" + usernameList.get(0) + "'";
		for (int i=1;i<usernameList.size();i++) {
			query+="and follower.follower_username IN (select follower_username from follower  where page_username='" + usernameList.get(i) + "')";
		}
		ResultSet rs = st.executeQuery(query);
		if(!rs.next()){
			return -1;
		}
		return rs.getInt("count");
	}


	public static void main(String[] args) throws Exception {
		Map<String,String> pages = new HashMap<String,String>();
		List<String> pagesUsername = new ArrayList<String>();
		pages.put("big.on.sight" , "2261283819");
		pages.put("elemental.on.sight" , "3424247923");
		pages.put("fujimoto.on.sight" , "3559358226");
		pages.put("sanaa.on.sight" , "3637326568");
		pages.put("herzogdemeuron.on.sight" , "3614860088");
		
		for(Map.Entry<String,String> currAccount : pages.entrySet()){
			String pageUsername = (String)currAccount.getKey();
			String pageId = (String)currAccount.getValue();
			List<String> followers = getPageFollowers(pageUsername,pageId);
			insertPageFollowers(pageUsername,followers);
		}

		pagesUsername.addAll(pages.keySet());
		System.out.println("********************** Joint Follower **********************");		
		for(int i=0;i<pages.size()-1;i++){
			for(int j=i+1;j<pages.size();j++){
				String pageUsername1 = pagesUsername.get(i);
				String pageUsername2 = pagesUsername.get(j);
				System.out.println(pageUsername1 + " and " + pageUsername2 + " : " + getJointFollowerCount(pageUsername1,pageUsername2));
			}
		}
		System.out.println("\nAll : " + getJointFollowerCount(pagesUsername));
		System.out.println("************************************************************");		

	}
}
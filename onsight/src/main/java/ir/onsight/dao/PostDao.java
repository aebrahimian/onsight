package ir.onsight.dao;

import ir.onsight.entity.Post;
import ir.onsight.entity.Post.MediaType;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostDao {
	private static final String CONN_STR = "jdbc:mysql://localhost:3306/onsight?user=onsight_access&password=onsightpass";
	static {
		try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load MySQL JDBC driver");
		}
	}
	
	public static void insertNewPost(Post post) throws SQLException{	
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("INSERT INTO post(creator_username,created_time,account_username,post_status,is_edited,media_type,media_path,"
																		+ "project_name_fa,project_name_en,code,program_fa,program_en,location_fa,location_en,architect_fa,"
																		+ "architect_en,year,size,project_status_fa,project_status_en,description_fa,description_en,keywords_fa,keywords_en)"
																		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
		preStmt.setString(1,post.getCreator().getUsername());
		preStmt.setTimestamp(2, new Timestamp(post.getCreatedTime().getTime()));
		preStmt.setString(3,post.getAccount().getUsername());
		preStmt.setString(4,post.getStatus().name());
		preStmt.setBoolean(5,post.isEdited());
		preStmt.setString(6,post.getMediaType().name());
		preStmt.setString(7,post.getMediaPath());
		preStmt.setString(8,post.getProjectNameFa());
		preStmt.setString(9,post.getProjectNameEn());
		preStmt.setString(10,post.getCode());
		preStmt.setString(11,post.getProgramFa());
		preStmt.setString(12,post.getProgramEn());
		preStmt.setString(13,post.getLocationFa());
		preStmt.setString(14,post.getLocationEn());
		preStmt.setString(15,post.getArchitectFa());
		preStmt.setString(16,post.getArchitectEn());
		preStmt.setInt(17,post.getYear());
		preStmt.setInt(18,post.getSize());
		preStmt.setString(19,post.getProjectStatusFa());
		preStmt.setString(20,post.getProjectStatusEn());
		preStmt.setString(21,post.getDescriptionFa());
		preStmt.setString(22,post.getDescriptionEn());
		preStmt.setString(23,post.getKeywordsFa());
		preStmt.setString(24,post.getKeywordsEn());
		preStmt.executeUpdate();
		
		preStmt = con.prepareStatement("SELECT LAST_INSERT_ID()"); 
		ResultSet postIdInfo = preStmt.executeQuery();
		if(postIdInfo.next()){
			post.setId(postIdInfo.getInt("LAST_INSERT_ID()"));
		}
		con.close();
	}
	
	public static void updateMediaInfo(int postId,String mediaPath,MediaType mediaType) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("UPDATE post SET media_path=?,media_type=? WHERE id=?"); 
		preStmt.setString(1, mediaPath);
		preStmt.setString(2, mediaType.name());
		preStmt.setInt(3, postId);
		preStmt.executeUpdate();
		con.close();
	}
	
	public static void deletePostById(int postId) throws SQLException{	
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("DELETE FROM post WHERE id=?"); 
		preStmt.setInt(1, postId);
		preStmt.executeUpdate();
		con.close();
	}
}

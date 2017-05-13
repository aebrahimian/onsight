package ir.onsight.dao;

import ir.onsight.entity.Account;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.MediaType;
import ir.onsight.entity.Post.PostStatus;
import ir.onsight.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.callback.ConfirmationCallback;

import org.apache.commons.lang3.StringUtils;

import com.mysql.cj.core.conf.StringPropertyDefinition;

public class PostDao {
	private static final String CONN_STR = "jdbc:mysql://localhost:3306/onsight?serverTimezone=UTC&user=onsight_access&password=onsightpass";
	static {
		try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load MySQL JDBC driver");
		}
	}

	public static void insertNewPost(Post post) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("INSERT INTO post(creator_username,confirmer_username,created_time,release_time,account_username,post_status,is_edited,edit_note,media_type,media_path,"
																		+ "project_name_fa,project_name_en,code,program_fa,program_en,location_fa,location_en,architect_fa,"
																		+ "architect_en,year,size,project_status_fa,project_status_en,description_fa,description_en,keywords_fa,keywords_en)"
																		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		preStmt.setString(1,post.getCreator()!=null ? post.getCreator().getUsername() : null);
		preStmt.setString(2,post.getConfirmer()!=null ? post.getConfirmer().getUsername() : null);
		preStmt.setTimestamp(3,post.getCreatedTime()!=null ? new Timestamp(post.getCreatedTime().getTime()) : null);
		preStmt.setTimestamp(4,post.getReleaseTime()!=null ? new Timestamp(post.getReleaseTime().getTime()) : null);
		preStmt.setString(5,post.getAccount()!=null ? post.getAccount().getUsername() : null);
		preStmt.setString(6,post.getStatus()!=null ? post.getStatus().name() : null);
		if (post.isEdited() != null) preStmt.setBoolean(7, post.isEdited()); else preStmt.setNull(7, Types.BIT);
		preStmt.setString(8,post.getEditNote());
		preStmt.setString(9,post.getMediaType()!=null ? post.getMediaType().name() : null);
		preStmt.setString(10,post.getMediaRelativePath());
		preStmt.setString(11,post.getProjectNameFa());
		preStmt.setString(12,post.getProjectNameEn());
		preStmt.setString(13,post.getCode());
		preStmt.setString(14,post.getProgramFa());
		preStmt.setString(15,post.getProgramEn());
		preStmt.setString(16,post.getLocationFa());
		preStmt.setString(17,post.getLocationEn());
		preStmt.setString(18,post.getArchitectFa());
		preStmt.setString(19,post.getArchitectEn());
		if (post.getYear() != null) preStmt.setInt(20, post.getYear()); else preStmt.setNull(20, Types.INTEGER);
		if (post.getSize() != null) preStmt.setInt(21, post.getSize()); else preStmt.setNull(21, Types.INTEGER);
		preStmt.setString(22,post.getProjectStatusFa());
		preStmt.setString(23,post.getProjectStatusEn());
		preStmt.setString(24,post.getDescriptionFa());
		preStmt.setString(25,post.getDescriptionEn());
		preStmt.setString(26,post.getKeywordsFa());
		preStmt.setString(27,post.getKeywordsEn());
		preStmt.executeUpdate();

		preStmt = con.prepareStatement("SELECT LAST_INSERT_ID()");
		ResultSet postIdInfo = preStmt.executeQuery();
		if(postIdInfo.next()){
			post.setId(postIdInfo.getInt("LAST_INSERT_ID()"));
		}
		con.close();
	}

	private static Post createPost(ResultSet postInfo) throws SQLException{
		Integer id = postInfo.getInt("id")!=0 ? postInfo.getInt("id") : null;
		User creator = postInfo.getString("creator_username")!=null ? new User(postInfo.getString("creator_username")) : null;
		User confirmer = postInfo.getString("confirmer_username")!=null ? new User(postInfo.getString("confirmer_username")) : null;;
		Date createdTime = postInfo.getTimestamp("created_time")!=null ? new Date(postInfo.getTimestamp("created_time").getTime()) : null;
		Date releaseTime = postInfo.getTimestamp("release_time")!=null ? new Date(postInfo.getTimestamp("release_time").getTime()) : null;
		Account account = postInfo.getString("account_username")!=null ? new Account(postInfo.getString("account_username")) : null;
		PostStatus postStatus = postInfo.getString("post_status")!=null ? PostStatus.valueOf(postInfo.getString("post_status")) : null;
		Boolean isEdited = postInfo.getBoolean("is_edited");
		String editNote = postInfo.getString("edit_note");
		MediaType mediaType = postInfo.getString("media_type")!=null ? MediaType.valueOf(postInfo.getString("media_type")) : null;
		String mediaPath = postInfo.getString("media_path");
		String projectNameFa = postInfo.getString("project_name_fa");
		String projectNameEn = postInfo.getString("project_name_en");
		String code = postInfo.getString("code");
		String programFa = postInfo.getString("program_fa");
		String programEn = postInfo.getString("program_en");
		String locationFa = postInfo.getString("location_fa");
		String locationEn = postInfo.getString("location_en");
		String architectFa = postInfo.getString("architect_fa");
		String architectEn = postInfo.getString("architect_en");
		Integer year = postInfo.getInt("year")!=0 ? postInfo.getInt("year") : null;
		Integer size = postInfo.getInt("size")!=0 ? postInfo.getInt("size") : null;
		String projectStatusFa= postInfo.getString("project_status_fa");
		String projectStatusEn= postInfo.getString("project_status_en");
		String descriptionFa = postInfo.getString("description_fa");
		String descriptionEn = postInfo.getString("description_en");
		String keywordsFa = postInfo.getString("keywords_fa");
		String keywordsEn = postInfo.getString("keywords_en");

		return new Post(id, creator, confirmer, createdTime, releaseTime, postStatus, isEdited, editNote,
				mediaType, mediaPath, null, account, projectNameFa, projectNameEn, code, programFa,
				programEn, locationFa, locationEn, architectFa, architectEn, year, size, projectStatusFa,
				projectStatusEn, descriptionFa, descriptionEn, keywordsFa, keywordsEn);
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

	public static boolean deletePost(int postId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("DELETE FROM post WHERE id=?");
		preStmt.setInt(1, postId);
		int rowCount = preStmt.executeUpdate();
		con.close();
		return rowCount > 0;
	}

	public static boolean chnagePostStatus(int postId, PostStatus newStatus) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("UPDATE post SET post_status=? where id=?");
		preStmt.setString(1, newStatus.name());
		preStmt.setInt(2, postId);
		int rowCount = preStmt.executeUpdate();
		con.close();
		return rowCount > 0;
	}

	public static List<Post> selectPosts(String username, List<PostStatus> postStatusList, Date startReleaseTime) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		String query = "SELECT * FROM post WHERE post_status in (";

		for(int i =0;i<postStatusList.size();i++){
			query+="?";
			if(i!=postStatusList.size()-1)
				query+=",";
		}
		query+=")";

		if(username != null)
			query+=" and creator_username = ?";

		if(startReleaseTime != null)
			query+=" and release_time <= ?";

		PreparedStatement preStmt = con.prepareStatement(query);

		int i;
		for (i = 0; i < postStatusList.size(); i++) {
			preStmt.setString(i+1, postStatusList.get(i).name());
		}

		if(username != null)
			preStmt.setString(++i, username);

		if(startReleaseTime != null)
			preStmt.setTimestamp(++i, new Timestamp(startReleaseTime.getTime()));
		ResultSet postsInfo = preStmt.executeQuery();
		List<Post> posts = new LinkedList<Post>();
		while(postsInfo.next()){
			posts.add(createPost(postsInfo));
		}
		con.close();
		return posts;
	}

	public static List<Post> selectPosts(PostStatus postStatus, Date startReleaseTime) throws SQLException{
		return selectPosts(null, Arrays.asList(postStatus), startReleaseTime);
	}

	public static List<Post> getUserPosts(String username, List<PostStatus> postStatusList) throws SQLException{
		return selectPosts(username, postStatusList, null);
	}

	public static List<Post> getAllPosts(List<PostStatus> postStatusList) throws SQLException{
		return getUserPosts(null, postStatusList);
	}

	public static List<Post> getPostsByStatus(PostStatus postStatus) throws SQLException{
		return getAllPosts(Arrays.asList(postStatus));
	}

	public static Post getPostById(int postId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT * FROM post WHERE id=?");
		preStmt.setInt(1, postId);
		ResultSet postInfo = preStmt.executeQuery();
		Post post = null ;
		if(postInfo.next())
			post = createPost(postInfo);
		con.close();
		return post;
	}

	public static Boolean isPostExist(int postId) throws SQLException {
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT id FROM post WHERE id=?");
		preStmt.setInt(1, postId);
		ResultSet postInfo = preStmt.executeQuery();
		Boolean isExist = false;
		if (postInfo.next())
			isExist = true;
		con.close();
		return isExist;
	}

	public static User getPostCreator(int postId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		PreparedStatement preStmt = con.prepareStatement("SELECT creator_username FROM post WHERE id=?");
		preStmt.setInt(1, postId);
		ResultSet userInfo = preStmt.executeQuery();
		User creator = null ;
		if(userInfo.next())
			creator = new User(userInfo.getString("creator_username"));
		con.close();
		return creator;
	}


	public static void updatePost(Post post) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		if(post == null || post.getId() == null)
			return;
		PreparedStatement preStmt = con.prepareStatement("UPDATE post SET creator_username=ifnull(?,creator_username),confirmer_username=ifnull(?,confirmer_username),"
														+ "created_time=ifnull(?,created_time),release_time=ifnull(?,release_time),"
														+ "account_username=ifnull(?,account_username),post_status=ifnull(?,post_status),"
														+ "is_edited=ifnull(?,is_edited),edit_note=ifnull(?,edit_note),"
														+ "media_type=ifnull(?,media_type),media_path=ifnull(?,media_path),"
														+ "project_name_fa=ifnull(?,project_name_fa),project_name_en=ifnull(?,project_name_en),"
														+ "code=ifnull(?,code),program_fa=ifnull(?,program_fa),program_en=ifnull(?,program_en),"
														+ "location_fa=ifnull(?,location_fa),location_en=ifnull(?,location_en),"
														+ "architect_fa=ifnull(?,architect_fa),architect_en=ifnull(?,architect_en),"
														+ "year=ifnull(?,year),size=ifnull(?,size),"
														+ "project_status_fa=ifnull(?,project_status_fa),project_status_en=ifnull(?,project_status_en),"
														+ "description_fa=ifnull(?,description_fa),description_en=ifnull(?,description_en),"
														+ "keywords_fa=ifnull(?,keywords_fa),keywords_en=ifnull(?,keywords_en) "
														+ "WHERE id=?");
		preStmt.setString(1,post.getCreator()!=null ? post.getCreator().getUsername() : null);
		preStmt.setString(2,post.getConfirmer()!=null ? post.getConfirmer().getUsername() : null);
		preStmt.setTimestamp(3,post.getCreatedTime()!=null ? new Timestamp(post.getCreatedTime().getTime()) : null);
		preStmt.setTimestamp(4,post.getReleaseTime()!=null ? new Timestamp(post.getReleaseTime().getTime()) : null);
		preStmt.setString(5,post.getAccount()!=null ? post.getAccount().getUsername() : null);
		preStmt.setString(6,post.getStatus()!=null ? post.getStatus().name() : null);
		if (post.isEdited() != null) preStmt.setBoolean(7, post.isEdited()); else preStmt.setNull(7, Types.BIT);
		preStmt.setString(8,post.getEditNote());
		preStmt.setString(9,post.getMediaType()!=null ? post.getMediaType().name() : null);
		preStmt.setString(10,post.getMediaRelativePath());
		preStmt.setString(11,post.getProjectNameFa());
		preStmt.setString(12,post.getProjectNameEn());
		preStmt.setString(13,post.getCode());
		preStmt.setString(14,post.getProgramFa());
		preStmt.setString(15,post.getProgramEn());
		preStmt.setString(16,post.getLocationFa());
		preStmt.setString(17,post.getLocationEn());
		preStmt.setString(18,post.getArchitectFa());
		preStmt.setString(19,post.getArchitectEn());
		if (post.getYear() != null) preStmt.setInt(20, post.getYear()); else preStmt.setNull(20, Types.INTEGER);
		if (post.getSize() != null) preStmt.setInt(21, post.getSize()); else preStmt.setNull(21, Types.INTEGER);
		preStmt.setString(22,post.getProjectStatusFa());
		preStmt.setString(23,post.getProjectStatusEn());
		preStmt.setString(24,post.getDescriptionFa());
		preStmt.setString(25,post.getDescriptionEn());
		preStmt.setString(26,post.getKeywordsFa());
		preStmt.setString(27,post.getKeywordsEn());
		preStmt.setInt(28,post.getId());
		preStmt.executeUpdate();
		con.close();
	}
}

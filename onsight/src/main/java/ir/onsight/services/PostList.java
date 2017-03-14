package ir.onsight.services;

import ir.onsight.dao.AccountDao;
import ir.onsight.dao.PostDao;
import ir.onsight.dao.UserDao;
import ir.onsight.entity.Account;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.PostStatus;

import javax.naming.ConfigurationException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.*;

@WebServlet("/posts")
public class PostList extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		List<Post> posts = null;
		try
		{
			String postStatus = req.getParameter("postStatus");
			String allUsers = req.getParameter("allUsers");
			boolean isAllUsers = false;
			if(StringUtils.isBlank(postStatus))
				postStatus = "ALL";
			if(!StringUtils.isBlank(allUsers) && allUsers.equalsIgnoreCase("true")){
				if(req.isUserInRole("admin")){
					isAllUsers = true;
				}
				else{
					resp.sendError(403);
					return;
				}
			}
			String[] statusStrList = postStatus.split(",");
			Set<PostStatus> postStatusSet = new HashSet<Post.PostStatus>();
			for(String statusStr : statusStrList){
				if(statusStr.equalsIgnoreCase("ALL")){
					for(PostStatus status : Arrays.asList(PostStatus.CONFIRMED,PostStatus.REJECTED,PostStatus.UNCONFIRMED,PostStatus.POSTED))
						postStatusSet.add(status);
				}else{
					postStatusSet.add(PostStatus.valueOf(statusStr.toUpperCase()));
				}
			}
			List<PostStatus> postStatusList = new ArrayList<PostStatus>(postStatusSet);
			if(isAllUsers)
				posts = PostDao.getAllPosts(postStatusList);
			else
				posts = PostDao.getUserPosts(req.getUserPrincipal().getName(), postStatusList);
			String mediaBaseUrl = getServletContext().getInitParameter("media_base_url");
			if(mediaBaseUrl == null)
				 throw new ConfigurationException();
			for(Post post : posts)
				post.setMediaWebUrlFromBase(mediaBaseUrl);
		}catch(SQLException e){
			message = "db problem";
			hasError = true;
			e.printStackTrace();
		}catch(ConfigurationException e){
			message = "bad config in server";
			hasError = true;
		}catch(IllegalArgumentException e){
			message = "wrong value of post status";
			hasError = true;
		}


		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError,message,"posts",posts).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
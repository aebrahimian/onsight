package ir.onsight.services;

import ir.onsight.dao.PostDao;
import ir.onsight.entity.Post;
import ir.onsight.entity.User;
import ir.onsight.entity.Post.PostStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.security.AccessControlException;
import java.sql.*;

@WebServlet("/delete_post")
public class DeletePost extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		try
		{
			int postId = Integer.parseInt(req.getParameter("id"));
			Post post = PostDao.getPostById(postId);
			if(post == null)
				throw new IllegalArgumentException("wrong id");
			String creatorUsername = post.getCreator() != null ? post.getCreator().getUsername() : null;
			PostStatus postStatus = post.getStatus();
			boolean isAdmin = req.isUserInRole("admin");
			if(!isAdmin && !req.getUserPrincipal().getName().equals(creatorUsername))
				throw new AccessControlException("it's not your post babe");
			//TODO can't delete after confirmed by user.may be he/she can.it's depend on ghad
			PostDao.chnagePostStatus(postId, PostStatus.DELETED);
		}
		catch(SQLException e){
			message = "db problem";
			hasError = true;
		}catch(NumberFormatException e){
			message = "wrong format or missing parameter";
			hasError = true;
		}catch(IllegalArgumentException | AccessControlException e){
			message = e.getMessage();
			hasError = true;
		}

		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError, message).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
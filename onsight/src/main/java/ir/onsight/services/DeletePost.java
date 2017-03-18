package ir.onsight.services;

import ir.onsight.dao.PostDao;
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
			User creator = PostDao.getPostCreator(postId);
			if(creator == null)
				throw new IllegalArgumentException("wrong id");
			if(!req.isUserInRole("admin") && !req.getUserPrincipal().getName().equals(creator.getUsername()))
				throw new AccessControlException("it's not your post babe");
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
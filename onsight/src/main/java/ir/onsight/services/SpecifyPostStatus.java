package ir.onsight.services;

import ir.onsight.dao.PostDao;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.PostStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.AccessControlException;
import java.sql.*;

@WebServlet("/specify_post_status")
public class SpecifyPostStatus extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		try
		{
			Integer id = Integer.parseInt(req.getParameter("id"));
			String statusStr = req.getParameter("status");
			Post post = new Post(id);

			if (StringUtils.isBlank(statusStr)) {
				throw new IllegalArgumentException("missing(empty) parameter");
			} else if (statusStr.equals("confirm")) {
				Date releaseTime = new Date(Long.parseLong(req.getParameter("releaseTime")));
				post.setStatus(PostStatus.CONFIRMED);
				post.setReleaseTime(releaseTime);
			} else if (statusStr.equals("reject")) {
				String editNote = req.getParameter("editNote");
				post.setStatus(PostStatus.REJECTED);
				post.setEditNote(editNote);
			} else {
				throw new IllegalArgumentException("wrong status");
			}

			if (!PostDao.isPostExist(id))
				throw new IllegalArgumentException("wrong id");

			PostDao.updatePost(post);
		}
		catch(SQLException e){
			message = "db problem";
			hasError = true;
		}catch(NumberFormatException e){
			message = "wrong format or missing parameter";
			hasError = true;
		}catch(IllegalArgumentException e){
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
package ir.onsight.services;

import ir.onsight.dao.UserDao;
import ir.onsight.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.util.List;
import java.sql.*;

@WebServlet("/unconfirmed_users")
public class UnconfirmedUsers extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		List<User> unconfirmedUsers = null;
		try
		{
			unconfirmedUsers = UserDao.findUnconfirmedUsers();				
		}
		catch(SQLException e){
			message = "db problem";
			hasError = true;			
		}			
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError,message,"unconfirmedUsers",unconfirmedUsers).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
package ir.onsight.services.web;

import ir.onsight.dao.UserDao;
import ir.onsight.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;

@WebServlet("/signup")
public class Signup extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String name = req.getParameter("name");
		String family = req.getParameter("family");
		if(username==null || name==null || family==null || password==null || 
		   username.equals("") || name.equals("") || family.equals("") || password.equals(""))
		{
			message = "wrong format or missing parameter";
			hasError = true;
		}
		else
		{
			try
			{
				User user = UserDao.findUserByUsername(username);				
				if(user!=null){	
					message = "repeated username";			
					hasError = true ;
				}else{				
					UserDao.insertNewUser(username, password, name, family, false);
				}
			}
			catch(SQLException e){
				message = "db problem";
				hasError = true;			
			}			
		}
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError, message).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
package ir.onsight.services;

import ir.onsight.dao.UserDao;
import ir.onsight.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;

@WebServlet("/specify_user_status")
public class SpecifyUserStatus extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		String username = req.getParameter("username");
		String status = req.getParameter("status");
		String rolesStr = req.getParameter("roles");
		List<String> roles;
		if(username==null || status==null || rolesStr==null ||
				username.equals("") || status.equals("") || rolesStr.equals(""))
		{
			message = "wrong format or missing parameter";
			hasError = true;
		}
		else
		{
			if(!(status.equals("confirm") || status.equals("reject"))){
				message = "wrong status";
				hasError = true;
			}
			roles = Arrays.asList(rolesStr.split(","));
			for(String currRoele : roles){
				if(!(currRoele.equals("user") || currRoele.equals("admin"))){
					message = "wrong role";
					hasError = true;
					break;
				}
			}
			if(!hasError){
				try
				{				
					if(!UserDao.isUserExist(username)){	
						message = "wrong username";			
						hasError = true ;
					}else if(status.equals("reject")){				
						UserDao.deleteUser(username);
					}else{
						UserDao.confirmUser(username,roles);
					}
				}
				catch(SQLException e){
					message = "db problem";
					hasError = true;			
				}			
			}
		}
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError, message).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
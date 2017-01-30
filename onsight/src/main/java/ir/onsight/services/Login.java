package ir.onsight.services;

import java.io.IOException;
import java.sql.SQLException;

import ir.onsight.dao.UserDao;
import ir.onsight.dao.UserDao.AuthResult;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/login")
public class Login extends HttpServlet {

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException , IOException {
		String message ="",username,password;
		boolean hasError=false;
		AuthResult authRes=null;
		username = req.getParameter("username");
		password = req.getParameter("password");
		if(username == null || password == null || username.equals("") || password.equals("")){
			hasError = true ;
			message = "wrong format or missing parameter" ;	
		}
		else
		{
			try{
				authRes = UserDao.authenticate(username, password);
			}catch(SQLException e){
				e.printStackTrace();
				hasError = true ;
				message = "db problem" ;
			}
		}
		if(!hasError){
			if(authRes == AuthResult.WRONG_PASS || authRes == AuthResult.WRONG_USER)
				message = "wrong username or password";
			else if(authRes == AuthResult.NOT_CONFIRMED)
				message = "your account has not been confirmed by admin yet";
			else if(authRes == AuthResult.SUCCESSFUL){
				try{
					req.logout();
					req.login(username, password);
				}catch(ServletException ex){
					hasError = true;
					message ="Internal Server Error";
				}
			}
		}
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError && authRes.getResult(), message).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
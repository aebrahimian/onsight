package ir.onsight.services.web;

import ir.onsight.dao.UserDao;
import ir.onsight.dao.UserDao.UserAuthResult;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class Login extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException , IOException {
		String message ="",username,password;
		boolean hasError=false;
		UserAuthResult authRes=null;
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
			if(authRes == UserAuthResult.WRONG_PASS || authRes == UserAuthResult.WRONG_USER)
				message = "wrong username or password";
			else if(authRes == UserAuthResult.NOT_CONFIRMED)
				message = "your account has not been confirmed by admin yet";
			else if(authRes == UserAuthResult.SUCCESSFUL){
				try{
					req.logout();
					req.login(username, password);
				}catch(ServletException ex){
					ex.printStackTrace();
					hasError = true;
					message ="Internal Server Error";
				}
			}
		}
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError && authRes.getResult(), message).toJson());
	}

	@Override
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
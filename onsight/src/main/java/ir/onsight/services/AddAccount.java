package ir.onsight.services;

import ir.onsight.dao.AccountDao;
import ir.onsight.dao.UserDao;
import ir.onsight.entity.Account;
import ir.onsight.entity.Account.AccountAuthResult;
import ir.onsight.entity.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;

@WebServlet("/add_account")
public class AddAccount extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if(username==null || password==null || 
		   username.equals("") || password.equals(""))
		{
			message = "wrong format or missing parameter";
			hasError = true;
		}
		else
		{
			try
			{
				Account account = AccountDao.findAccountByUsername(username);				
				if(account!=null){	
					message = "repeated username";			
					hasError = true ;
				}else{				
					account = new Account(username,password);
					AccountAuthResult authRes = account.authenticate();
					if(authRes==AccountAuthResult.SUCCESSFUL){
						AccountDao.insertNewAccount(username, password);
					}else{
						switch (authRes) {
						case WRONG_USER_PASS:
							message="username or password of account is wrong";
							break;
						case ACCOUNT_PROBLEM:
							message="account has some problem.check or verify your account";
							break;
						case NET_ERROR:
							message="some network problem with authentication server";
							break;							
						}
						hasError = true;
					}
				}
			}
			catch(SQLException e){
				message = "db problem";
				hasError = true;	
			}		
			catch (IOException e) {
				message = "server network problem";
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
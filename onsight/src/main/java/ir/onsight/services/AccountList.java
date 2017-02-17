package ir.onsight.services;

import ir.onsight.dao.AccountDao;
import ir.onsight.dao.UserDao;
import ir.onsight.entity.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.util.List;
import java.sql.*;

@WebServlet("/accounts")
public class AccountList extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		List<Account> accounts = null;
		try
		{
			accounts = AccountDao.getAllAccount();				
		}
		catch(SQLException e){
			message = "db problem";
			hasError = true;			
		}			
		resp.setContentType("text/html");
		resp.getWriter().println(new Response(!hasError,message,"accounts",accounts).toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
package ir.onsight.services;

import ir.onsight.dao.AccountDao;
import ir.onsight.dao.PostDao;
import ir.onsight.dao.UserDao;
import ir.onsight.entity.Account;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.*;
import ir.onsight.entity.User;

import javax.naming.ConfigurationException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.sql.*;

@WebServlet("/register_post")
@MultipartConfig(maxFileSize=-1L)
public class RegisterPost extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		Post post = null;
		try{
			Part mediaPart = req.getPart("media");
			String accountUsername = req.getParameter("accountUsername");
			String projectNameFa = req.getParameter("projectNameFa");
			String projectNameEn = req.getParameter("projectNameEn");
			String code = req.getParameter("code");
			String programFa = req.getParameter("programFa");
			String programEn = req.getParameter("programEn");
			String locationFa = req.getParameter("locationFa");
			String locationEn = req.getParameter("locationEn");
			String architectFa = req.getParameter("architectFa");
			String architectEn = req.getParameter("architectEn");
			int year = Integer.parseInt(req.getParameter("year"));
			int size = Integer.parseInt(req.getParameter("size"));
			String projectStatusFa = req.getParameter("projectStatusFa");
			String projectStatusEn = req.getParameter("projectStatusEn");
			String descriptionFa = req.getParameter("descriptionFa");
			String descriptionEn = req.getParameter("descriptionEn");
			String keywordsFa = req.getParameter("keywordsFa");
			String keywordsEn = req.getParameter("keywordsEn");
			String creatorUsername = req.getUserPrincipal().getName();
			if(StringUtils.isBlank(accountUsername) || StringUtils.isBlank(projectNameFa) || StringUtils.isBlank(projectNameEn) ||
			   StringUtils.isBlank(code) || StringUtils.isBlank(programFa) || StringUtils.isBlank(programEn) ||
			   StringUtils.isBlank(locationFa) || StringUtils.isBlank(locationEn) || StringUtils.isBlank(architectFa) ||
			   StringUtils.isBlank(architectEn) || StringUtils.isBlank(projectStatusFa) || StringUtils.isBlank(projectStatusEn) ||
			   StringUtils.isBlank(descriptionFa) || StringUtils.isBlank(descriptionEn) || StringUtils.isBlank(keywordsFa) ||
			   StringUtils.isBlank(keywordsEn) || mediaPart == null)
			{
				throw new IllegalArgumentException("missing(empty) parameter");
			}
			if(AccountDao.findAccountByUsername(accountUsername) == null){
				throw new IllegalArgumentException("no account found with this username");
			}
			MediaType mediaType = getMediaType(mediaPart);
			if(mediaType == MediaType.OTHER)
				throw new IllegalArgumentException("uploaded file has wrong type.only image and video is acceptable");
			post = new Post(new User(creatorUsername),mediaType, null, new Account(accountUsername), projectNameFa,
					 		projectNameEn,code,programFa, programEn,locationFa,locationEn,architectFa, architectEn, year, size,
					 		projectStatusFa,projectStatusEn, descriptionFa,descriptionEn, keywordsFa, keywordsEn);
			PostDao.insertNewPost(post);
			String mediaBasePath = getServletContext().getInitParameter("media_base_path");
			Path mediaPath = getMediaPath(mediaPart, post, mediaBasePath);
			storeMedia(mediaPart, mediaPath);
			post.setMediaRelativePathFromFileName(mediaPart.getSubmittedFileName());
			String mediaBaseUrl = getServletContext().getInitParameter("media_base_url");
			if(mediaBaseUrl == null)
				 throw new ConfigurationException();
			post.setMediaWebUrlFromBase(mediaBaseUrl);
			PostDao.updateMediaInfo(post.getId(), post.getMediaRelativePath(), mediaType);
		}catch(SQLException e){
			//TODO maybe sqlexception occurred in the previous line .so we should delete the post
			message = "db problem";
			hasError = true;
		}catch(NumberFormatException e){
			message = "wrong format or missing parameter";
			hasError = true;
		}catch(IllegalArgumentException e){
			message = e.getMessage();
			hasError = true;
		}catch(IOException | ServletException | ConfigurationException e){
			message = "error in receiving uploaded file(bad config in client or server)";
			hasError = true;
			e.printStackTrace();
			try {
				PostDao.deletePost(post.getId());
			} catch (Exception e1) {
				//TODO log or print stack trace;
			}
		}catch(IllegalStateException e){
			message = "uploaded file size exceeded the limit";
			hasError = true;
		}
		resp.setContentType("text/html");
		Response response = hasError ? new Response(false, message) : new Response(true, message, "post", post);
		resp.getWriter().println(response.toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}

	public static Path getMediaPath(Part mediaPart, Post post, String mediaBasePath) throws IOException,ConfigurationException{
		 String mediaFileName = mediaPart.getSubmittedFileName();
		 int mediaId = post.getId();
		 if(mediaBasePath == null || Files.notExists(Paths.get(mediaBasePath)))
			 throw new ConfigurationException();
		 Path accountPath = Paths.get(mediaBasePath,post.getAccount().getUsername());
		 if(Files.notExists(accountPath))
			 Files.createDirectory(accountPath);
		 return accountPath.resolve(Integer.toString(mediaId) + "_" + mediaFileName);
	}

	public static MediaType getMediaType(Part mediaPart){
		String mediaType = mediaPart.getContentType().split("/")[0];
		if(mediaType.equals("video"))
			return MediaType.VIDEO;
		else if(mediaType.equals("image"))
			return MediaType.IMAGE;
		else
			return MediaType.OTHER;
	}

	public static void storeMedia(Part mediaPart,Path mediaPath) throws IOException{
		InputStream mediaContent = mediaPart.getInputStream();
	    CopyOption[] options = new CopyOption[] {
	    	StandardCopyOption.REPLACE_EXISTING
		};
		Files.copy(mediaContent,mediaPath,options);
	}


}
package ir.onsight.services.web;

import ir.onsight.dao.AccountDao;
import ir.onsight.dao.PostDao;
import ir.onsight.entity.Account;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.*;

import javax.naming.ConfigurationException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.nio.file.Path;
import java.security.AccessControlException;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/edit_post")
@MultipartConfig(maxFileSize=-1L)
public class EditPost extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "";
		Post post = null;
		try{
			Integer postId = Integer.parseInt(req.getParameter("id"));
			post = PostDao.getPostById(postId);
			if(post == null)
				throw new IllegalArgumentException("wrong id");
			String creatorUsername = post.getCreator() != null ? post.getCreator().getUsername() : null;
			boolean isAdmin = req.isUserInRole("admin");
			if(req.getUserPrincipal() == null || (!isAdmin && !req.getUserPrincipal().getName().equals(creatorUsername)))
				throw new AccessControlException("it's not your post babe");
			Part mediaPart;
			try{
				mediaPart = req.getPart("media");
			}catch(ServletException e){
				mediaPart = null;
			}
			//TODO access control. ghad :can user edit post after you confirmed it ? (-> unconfirmed)
			//TODO access control. ghad :can user change account after you confirmed it (-> unconfirmed but may not notice)?
			Date releaseTime = req.getParameter("releaseTime") !=null ? new Date(Long.parseLong(req.getParameter("releaseTime"))) : null;
			Account account = req.getParameter("accountUsername") !=null ? new Account(req.getParameter("accountUsername")) : null;
			String editNote = req.getParameter("editNote");
			String projectNameFa = req.getParameter("projectNameFa");
			String projectNameEn = req.getParameter("projectNameEn");
			String code = req.getParameter("code");
			String programFa = req.getParameter("programFa");
			String programEn = req.getParameter("programEn");
			String locationFa = req.getParameter("locationFa");
			String locationEn = req.getParameter("locationEn");
			String architectFa = req.getParameter("architectFa");
			String architectEn = req.getParameter("architectEn");
			Integer year = req.getParameter("year") !=null ? Integer.parseInt(req.getParameter("year")) : null;
			Integer size = req.getParameter("size") !=null ? Integer.parseInt(req.getParameter("size")) : null;
			String projectStatusFa = req.getParameter("projectStatusFa");
			String projectStatusEn = req.getParameter("projectStatusEn");
			String descriptionFa = req.getParameter("descriptionFa");
			String descriptionEn = req.getParameter("descriptionEn");
			String keywordsFa = req.getParameter("keywordsFa");
			String keywordsEn = req.getParameter("keywordsEn");
			boolean isEdited = true;
			PostStatus postStatus = null;
			//TODO access control. ghad :can user edit post after it is deleted or it is posted (change the post on the website)
			if(!isAdmin && post.getStatus() != PostStatus.DELETED && post.getStatus() != PostStatus.POSTED)
				postStatus = PostStatus.UNCONFIRMED;

			if(account !=null && AccountDao.findAccountByUsername(account.getUsername()) == null){
				throw new IllegalArgumentException("no account found with this username");
			}
			if(!isAdmin && releaseTime != null)
				throw new AccessControlException("only admin can change post release time");
			if(!isAdmin && editNote != null)
				throw new AccessControlException("only admin can change post edit note");

			MediaType mediaType = null;
			if(mediaPart != null){
				mediaType = RegisterPost.getMediaType(mediaPart);
				if(mediaType == MediaType.OTHER)
					throw new IllegalArgumentException("uploaded file has wrong type.only image and video is acceptable");
			}

			post.updateFields(null, null, null, releaseTime, postStatus, isEdited, editNote, mediaType,
							  null, null, account, projectNameFa, projectNameEn, code, programFa, programEn,
							  locationFa, locationEn, architectFa, architectEn, year, size, projectStatusFa, projectStatusEn,
							  descriptionFa, descriptionEn, keywordsFa, keywordsEn);
			if(mediaPart != null){
				String mediaBasePath = getServletContext().getInitParameter("media_base_path");
				Path mediaPath = RegisterPost.getMediaPath(mediaPart, post, mediaBasePath);
				RegisterPost.storeMedia(mediaPart, mediaPath);
				post.setMediaRelativePathFromFileName(mediaPart.getSubmittedFileName());
			}
			String mediaBaseUrl = getServletContext().getInitParameter("media_base_url");
			if(mediaBaseUrl == null)
				throw new ConfigurationException();
			post.setMediaWebUrlFromBase(mediaBaseUrl);
			PostDao.updatePost(post);
		}catch(SQLException e){
			message = "db problem";
			hasError = true;
		}catch(NumberFormatException e){
			message = "wrong format or missing parameter";
			hasError = true;
		}catch(IllegalArgumentException | AccessControlException e){
			message = e.getMessage();
			hasError = true;
		}catch(IOException | ConfigurationException e){
			message = "error in receiving uploaded file(bad config in client or server)";
			hasError = true;
			e.printStackTrace();
		}catch(IllegalStateException e){
			message = "uploaded file size exceeded the limit";
			hasError = true;
		}catch(Exception e){
			e.printStackTrace();
			message = "Server Internal Error";
			hasError = true;
		}
		resp.setContentType("text/html");
		Response response = hasError ? new Response(false, message) : new Response(true, message, "post", post);
		resp.getWriter().println(response.toJson());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}



}
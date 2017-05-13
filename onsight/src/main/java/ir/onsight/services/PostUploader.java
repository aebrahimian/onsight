package ir.onsight.services;

import ir.onsight.dao.PostDao;
import ir.onsight.entity.Post;
import ir.onsight.entity.Post.PostStatus;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PostUploader implements ServletContextListener, Runnable{
	private ScheduledExecutorService scheduler;
	private ServletContext servletContext;
	private final int callPeriodMin = 1;

	@Override
    public void contextInitialized(ServletContextEvent event) {
		this.servletContext = event.getServletContext();
		scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this , 0, callPeriodMin, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        scheduler.shutdownNow();
    }

    @Override
	public void run() {
    	System.out.println("running post uploader at " + new Date().toString());
		uploadConfirmedPosts();
	}

    public void uploadConfirmedPosts(){
    	try {
			List<Post> readyPosts = PostDao.selectPosts(PostStatus.CONFIRMED, new Date());
			String mediaBasePath =  this.servletContext.getInitParameter("media_base_path");
			for (Post post : readyPosts){
				post.setMediaAbsolutePathFromBase(mediaBasePath);
				post.getAccount().loadPassword();
				post.upload();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

}

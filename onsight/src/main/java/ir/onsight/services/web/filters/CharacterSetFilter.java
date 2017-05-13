package ir.onsight.services.web.filters;

import javax.servlet.*;  
import javax.servlet.FilterChain;  
import javax.servlet.FilterConfig;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRequest;  
import javax.servlet.ServletResponse;  
import java.io.IOException;  

/***  
 * This is a filter class to force the java webapp to handle all requests and responses as UTF-8 encoded by default.  
 * This requires that we define a character set filter.  
 * This filter makes sure that if the browser hasn't set the encoding used in the request, that it's set to UTF-8.  
 */  
public class CharacterSetFilter implements Filter {  

  private static final String UTF8 = "UTF-8";  
  private static final String CONTENT_TYPE = "text/html; charset=UTF-8";  
  private String encoding;  

  public void init(FilterConfig config) throws ServletException {  
    encoding = config.getInitParameter("requestEncoding");  
    if (encoding == null) {  
      encoding = UTF8;  
    }  
  }  

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
    // Honour the client-specified character encoding  
    if (null == request.getCharacterEncoding()) {  
      request.setCharacterEncoding(encoding);  
    }  
    /**  
     * Set the default response content type and encoding  
     */  
    response.setContentType(CONTENT_TYPE);  
    response.setCharacterEncoding(UTF8);  
    chain.doFilter(request, response);  
  }  

  public void destroy() {  
  } 

}
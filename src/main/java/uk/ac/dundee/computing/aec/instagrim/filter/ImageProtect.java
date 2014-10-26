package uk.ac.dundee.computing.aec.instagrim.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.lib.AuthenticateImage;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Servlet Filter implementation class ImageProtect
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD, 
				DispatcherType.INCLUDE
		}
				, urlPatterns = { "/rImage/*", "/Images/*", "/rThumb/*", "/Image/*", "/rProfileImage/*"})
public class ImageProtect implements Filter {
	private HashMap CommandsMap = new HashMap();
    /**
     * Default constructor. 
     */
    public ImageProtect() {
        CommandsMap.put("rImage", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("rThumb", 3);
        CommandsMap.put("Image", 4);
        CommandsMap.put("rProfileImage", 5);
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		String args[] = Convertors.SplitRequestPath((HttpServletRequest)request);
		
		HttpSession session = ((HttpServletRequest) request).getSession();		
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		if(lg == null)
			chain.doFilter(request, response); // another filter is going to handle this
		String LoggedInUser = lg.getUsername();
	  
		HttpServletRequest httpRequest = (HttpServletRequest) request;        
	       if(httpRequest.getMethod().equalsIgnoreCase("POST")){
	    	   chain.doFilter(request, response);
	    	   return;
	       }
		
    	
    	
    	
    	AuthenticateImage ai = new AuthenticateImage();
    	ai.setCluster(CassandraHosts.getCluster());
    	
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        
        
        switch (command) {
            case 1://user, image
            	if (!ai.authenticate(LoggedInUser, args[2], args[3]))
            		error("Dont even try it :)", response);
                break;
            case 2:
            	
                break;
            case 3:
            	if (!ai.authenticate(LoggedInUser, args[2], args[3]))
            		error("Dont even try it :)", response);
                break;
            case 4:
            	if (!ai.authenticate(LoggedInUser, args[2], args[3]))
            		error("Dont even try it :)", response);
                break;
            case 5:
            	
                break;
            default:
                error("Bad Operator", response);
        }
		chain.doFilter(request, response);
	}
	
	
	
	
	
    private void error(String mess, ServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

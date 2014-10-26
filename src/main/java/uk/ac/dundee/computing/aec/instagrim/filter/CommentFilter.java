package uk.ac.dundee.computing.aec.instagrim.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.lib.AuthenticateImage;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Servlet Filter implementation class CommentFilter
 */
@WebFilter(dispatcherTypes = {
		DispatcherType.REQUEST, 
		DispatcherType.FORWARD, 
		DispatcherType.INCLUDE
}
		, urlPatterns = { "/Comment/*"})

public class CommentFilter implements Filter {

    /**
     * Default constructor. 
     */
    public CommentFilter() {
        // TODO Auto-generated constructor stub
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		String args[] = Convertors.SplitRequestPath((HttpServletRequest)request);
		HttpSession session = ((HttpServletRequest) request).getSession();		
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		if(lg == null)
			chain.doFilter(request, response); // another filter is going to handle this
		String LoggedInUser = lg.getUsername();
	  
		HttpServletRequest httpRequest = (HttpServletRequest) request;        
	       if(httpRequest.getMethod().equalsIgnoreCase("POST")){
	    	   chain.doFilter(request, response);
	       }
	       else
	       {
	    
				    AuthenticateImage ai = new AuthenticateImage();
				    ai.setCluster(CassandraHosts.getCluster());
				    
			    	if (!ai.authenticate(LoggedInUser, args[2], args[3]))
			    		error("Dont even try it :)", response);
					// pass the request along the filter chain
					chain.doFilter(request, response);
	       }
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

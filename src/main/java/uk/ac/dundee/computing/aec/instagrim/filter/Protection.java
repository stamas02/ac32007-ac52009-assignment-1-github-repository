package uk.ac.dundee.computing.aec.instagrim.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 * Servlet Filter implementation class Protection
 */
@WebFilter(dispatcherTypes = {
				DispatcherType.REQUEST, 
				DispatcherType.FORWARD, 
				DispatcherType.INCLUDE
		}
					, urlPatterns = { "/*" })
public class Protection implements Filter {

	
	
    /**
     * Default constructor. 
     */
    public Protection() {
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

		
		
		String path = ((HttpServletRequest) request).getRequestURI();
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session=httpReq.getSession(false);
        LoggedIn li= null;
	    if (session!= null)
        li=(LoggedIn)session.getAttribute("LoggedIn");
	    
		System.out.println(path);
		if ((path.startsWith(httpReq.getContextPath()+"/Login"))||
		    (path.startsWith(httpReq.getContextPath()+"/login.jsp"))||
		    (path.startsWith(httpReq.getContextPath()+"/register.jsp"))||
		    (path.startsWith(httpReq.getContextPath()+"/Register")))
		{
	        if ((li == null)  || (li.getlogedin()==false))
				chain.doFilter(request, response);
			else
			{
	               RequestDispatcher rd=request.getRequestDispatcher("/");
	               rd.forward(request,response);  
			}
				
		}
		else if (path.equals(httpReq.getContextPath()+"/")||
				(path.endsWith("css")))
		{
			
			chain.doFilter(request, response);
		}
		else
		{		
	        System.out.println("Session in filter "+session);
	        if ((li == null)  || (li.getlogedin()==false))
	        {
	               System.out.println("Foward to login" + httpReq.getContextPath());
	               RequestDispatcher rd=request.getRequestDispatcher("/");
	               rd.forward(request,response);      
	        }
	        else
	        	chain.doFilter(request, response);
		}
        
        /*
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
	    // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }
        


	// If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            //sendProcessingError(problem, response);
        }
        */
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

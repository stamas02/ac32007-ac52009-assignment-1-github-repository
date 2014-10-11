package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.LoginChecker;
import uk.ac.dundee.computing.aec.instagrim.models.UserModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;


import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Profile
 */
@WebServlet({ "/Profile", "/Profile/*" })
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Cluster cluster=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        cluster = CassandraHosts.getCluster();
    }

   
  

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
	
		if (LoginChecker.ValidateSession(request))
		{
			DisplayProfile(args[1],LoginChecker.LoggedInUser(request),request,response);
		}
		else
		{
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
	        rd.forward(request, response);
		}
			
		
		
		
	}
	
	private void DisplayProfile(String profile_username,String username, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		UserModel user = new UserModel();
		user.setCluster(cluster);
		UserProfile userprofile =  user.getUserprofile("stamas01");
		RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
        request.setAttribute("userprofile", userprofile);
        rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

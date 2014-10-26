package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.JsonRenderer;
import uk.ac.dundee.computing.aec.instagrim.models.UserModel;
import uk.ac.dundee.computing.aec.instagrim.stores.SFolder;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

import com.datastax.driver.core.Cluster;

/**
 * Servlet implementation class Users
 */
@WebServlet({ "/Users", "/Users/*", "/Profile", "/Profile/*" })
public class Users extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;   
	private HashMap CommandsMap = new HashMap();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Users() {
        super();
        CommandsMap.put("Users", 1);
        CommandsMap.put("Profile", 2);


        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		cluster = CassandraHosts.getCluster();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String args[] = Convertors.SplitRequestPath(request);
		boolean isJson = args[args.length-1].equals("json");
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        
        switch (command) {
        case 1:
    		if (isJson)
    			userListjson(request, response);
    		else
    			userListjsp(request, response);
            break;
        case 2:
        	if (isJson)
        		profilejson(args[2], request, response);
        	else
        		profilejsp(args[2],  request, response);
            break;
        default:
            error("Bad Operator", response);
    }
		 

	}

	/**
	 /Users/json
	 */
	private void profilejson(String User, HttpServletRequest request, HttpServletResponse respons)throws ServletException, IOException 
	{
		UserModel usermodel = new UserModel();
		usermodel.setCluster(cluster);
		UserProfile userprofiles = usermodel.getUserprofile(User);
	
       String jasonResponse = JsonRenderer.render(userprofiles);       
       respons.setContentType("application/json");
		respons.setHeader("Content-Type", "application/json");  
		PrintWriter out = respons.getWriter();
		out.write(jasonResponse);
       

	}
	
	/**
	 /Profile/user
	 */
	private void profilejsp(String User, HttpServletRequest request, HttpServletResponse respons)throws ServletException, IOException 
	{
		RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
		request.setAttribute("User", User);
		rd.forward(request, respons);
	}
	
	/**
	 /Profile/user/json
	 */
	private void userListjson(HttpServletRequest request, HttpServletResponse respons)throws ServletException, IOException 
	{
		UserModel usermodel = new UserModel();
		usermodel.setCluster(cluster);
		java.util.LinkedList<UserProfile> userprofiles = usermodel.getAllUserprofiles();
	
        String jasonResponse = JsonRenderer.render(userprofiles);       
        respons.setContentType("application/json");
		respons.setHeader("Content-Type", "application/json");  
		PrintWriter out = respons.getWriter();
		out.write(jasonResponse);
	}
	
	/**
	 /Users
	 */
	private void userListjsp(HttpServletRequest request, HttpServletResponse respons)throws ServletException, IOException 
	{
		RequestDispatcher rd = request.getRequestDispatcher("/UserList.jsp");
        rd.forward(request, respons);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
	}

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}

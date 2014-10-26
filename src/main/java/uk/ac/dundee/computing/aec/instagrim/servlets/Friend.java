package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.JsonRenderer;
import uk.ac.dundee.computing.aec.instagrim.models.FriendModel;
import uk.ac.dundee.computing.aec.instagrim.models.MessageModel;
import uk.ac.dundee.computing.aec.instagrim.models.UserModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

/**
 * Servlet implementation class Friend
 */


@WebServlet({ "/Friends", "/Friends/*" , "/Friend/*", "/FriendRequest/*", "/AcceptFriend/*", "/Requests/*" })
public class Friend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cluster cluster;
	private HashMap CommandsMap = new HashMap();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Friend() {
        super();
        CommandsMap.put("Friends", 1);
        CommandsMap.put("Friend", 2);
        CommandsMap.put("FriendRequest", 3);
        CommandsMap.put("AcceptFriend", 4);
        CommandsMap.put("Requests", 5);
        
        cluster = CassandraHosts.getCluster();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String args[] = Convertors.SplitRequestPath(request);
		boolean isJson = args[args.length-1].equals("json");
		
		HttpSession session = request.getSession();
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		String LoggedInUser = lg.getUsername();
        
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
        		friendlistjson(LoggedInUser,args[2], response);
            break;
        case 2:
        	if (isJson)
        	{
        		int f = CheckRelationship(LoggedInUser,args[2]);
        		sendRelationship(f,request,response);
        	}
            break;
        case 3:
            sendFriendRequest(LoggedInUser,args[2]);
            response.sendRedirect(request.getContextPath()+"/Profile/"+args[2]);
            break;
        case 4:
        	AddFriend(LoggedInUser,args[2]);
            response.sendRedirect(request.getContextPath()+"/Profile/"+args[2]);
            break;
        case 5:
        	if (isJson)
        		requestListjson(LoggedInUser,args[2],response);
            //response.sendRedirect("/Instagrim/Profile/"+args[2]);
            break;
        default:
            error("Bad Operator", response);
        }
		
	}

	
	private void friendlistjson(String LoggedInUser, String user, HttpServletResponse response)throws ServletException, IOException
	{
		
		if (LoggedInUser.equals(user))
		{
			FriendModel friendmodel = new FriendModel();
			friendmodel.setCluster(cluster);
			java.util.LinkedList<UserProfile> myfriends = friendmodel.getFriendList(user);
			String jasonResponse = JsonRenderer.render(myfriends);
			
			response.setContentType("application/json");
			response.setHeader("Content-Type", "application/json");  
			PrintWriter out = response.getWriter();
			out.write(jasonResponse);
		}
		
	}
	
	private void AddFriend(String user1, String user2)
	{
		if(CheckRelationship(user1,user2) == 3)
		{
			FriendModel friendmodel = new FriendModel();
			friendmodel.setCluster(cluster);
			friendmodel.AcceptFriendRequest(user1, user2);
		}
	}
	
	private void sendFriendRequest(String user1, String user2)
	{
		if(CheckRelationship(user1,user2) == 0)
		{
			MessageModel message = new MessageModel();
			message.setCluster(cluster);
			message.sendFriendRequest(user1, user2);
		}
		
	}

	private void requestListjson(String LoggedInUser,String User, HttpServletResponse response)throws ServletException, IOException
	{
		System.out.println(LoggedInUser +"---"+ User);
		 if (!LoggedInUser.equals(User))
		 {
			 PrintWriter out = response.getWriter();
			 out.write("");
			 return;
		 }
		response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json");  
		
		MessageModel message = new MessageModel();
		message.setCluster(cluster);
		java.util.LinkedList<UserProfile> requests = message.getFriendRequests(LoggedInUser);
		String jasonResponse = JsonRenderer.render(requests);
		
		
		response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json");  
		PrintWriter out = response.getWriter();
		out.write(jasonResponse);
		
	}
	

	
	
	private void sendRelationship(int friend, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json");  
		PrintWriter out = response.getWriter();
		String res = "{\"friendstatus\":\"" + friend + "\"}";
		out.write(res);		
	}
	
    private int CheckRelationship(String user1, String user2)
	{
    	FriendModel friedmodel = new FriendModel();
    	friedmodel.setCluster(cluster);
		int friend = 0;
		if (user1.equals(user2))
			return -1;
		if (!friedmodel.IsFriend(user1, user2))
		{
			MessageModel messagemodel = new MessageModel();
			messagemodel.setCluster(cluster);
			if (!messagemodel.isFriendRequestSent(user1, user2))
			{
				if(messagemodel.isFriendRequestSent(user2, user1))
					friend = 3;
			}
			else
				friend = 2;
		}
		else
			friend=1;
		
		return friend;
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

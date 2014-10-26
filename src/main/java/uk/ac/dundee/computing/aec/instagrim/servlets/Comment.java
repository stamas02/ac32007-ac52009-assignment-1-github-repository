package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.JsonRenderer;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.SComment;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

/**
 * Servlet implementation class Comment
 */
@WebServlet(urlPatterns = {
	    "/Comment",
	    "/Comment/*",
	})
public class Comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Comment() {
        super();
        cluster = CassandraHosts.getCluster();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
        String args[] = Convertors.SplitRequestPath(request);
        boolean isJson = args[args.length-1].equals("json");
        java.util.UUID UUID = java.util.UUID.fromString(args[3]);
		commentlistjson(UUID,request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		String LoggedInUser = lg.getUsername();
		
    	java.util.UUID UUID = java.util.UUID.fromString(request.getParameter("PicID"));
    	String User = request.getParameter("User"); //to be able to redirect back to the pic
    	String myComment = request.getParameter("Comment");
    			
		PostAComment(myComment,UUID, LoggedInUser);
		redirectToImage(UUID,User, request,response);
		
		
	}
	
	
	private void redirectToImage(java.util.UUID UUID,String User,HttpServletRequest request, HttpServletResponse response)
	{
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        try {
			response.sendRedirect(request.getContextPath()+"/Image/" + User + "/"+ UUID.toString());
		} catch (IOException e) {
			// TODO Auto-geinerated catch block
			e.printStackTrace();
		}	
	}
	private void commentlistjson(java.util.UUID UUID,HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		PicModel tm = new PicModel();
		tm.setCluster(cluster);      
		LinkedList<SComment> comments = tm.getPicComments(UUID);
		String jasonResponse = JsonRenderer.render(comments);
	       
	    response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json");  
		PrintWriter out = response.getWriter();
		out.write(jasonResponse);
	}

    
	private void PostAComment(String myComment,java.util.UUID UUID, String User)
	{
		 PicModel tm = new PicModel();
	     tm.setCluster(cluster);
	     tm.PostAComment(myComment, UUID, User);
	        
	}

}

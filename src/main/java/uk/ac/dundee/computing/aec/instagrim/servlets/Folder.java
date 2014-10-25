package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.JsonRenderer;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.SFolder;

/**
 * Servlet implementation class Folder
 */
@WebServlet({ "/Folder", "/Folder/*" })
public class Folder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private Cluster cluster;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Folder() {
        super();
        // TODO Auto-generated constructor stub
        cluster = CassandraHosts.getCluster();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String args[] = Convertors.SplitRequestPath(request);
		 boolean isJson = args[args.length-1].equals("json");
		 

		if (isJson)
			folderListjson(args[2], request, response);
		else
			folderListjsp(args[2], request, response);
	}
	
	public void folderListjson(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PicModel tm = new PicModel();
		tm.setCluster(cluster);
		
		java.util.LinkedList<SFolder> lsFolders = tm.getUserPicsFolders(User);
		
        String jasonResponse = JsonRenderer.render(lsFolders);       
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.write(jasonResponse);
		
	}
	
	public void folderListjsp(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher rd = request.getRequestDispatcher("/UsersFolders.jsp");
        request.setAttribute("User", User);
        rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

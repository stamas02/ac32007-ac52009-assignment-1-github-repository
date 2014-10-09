package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.LoginChecker;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

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
		if (!LoginChecker.ValidateSession(request))
		{
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
	        rd.forward(request, response);
		}
		
		DisplayFolders(args[2], request, response);
	}
	
	public void DisplayFolders(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PicModel tm = new PicModel();
		tm.setCluster(cluster);
		
		java.util.LinkedList<String> lsFolders = tm.getUserPicsFolders(User);
		RequestDispatcher rd = request.getRequestDispatcher("/UsersFolders.jsp");
        request.setAttribute("Folders", lsFolders);
        rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

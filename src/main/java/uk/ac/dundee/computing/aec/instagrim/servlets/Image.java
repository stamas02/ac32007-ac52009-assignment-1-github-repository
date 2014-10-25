package uk.ac.dundee.computing.aec.instagrim.servlets;


import com.datastax.driver.core.Cluster;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import uk.ac.dundee.computing.aec.instagrim.lib.AuthenticateImage;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.JsonRenderer;
import uk.ac.dundee.computing.aec.instagrim.models.FriendModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.SComment;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/rImage/*",
    "/Thumb/*",
    "/rThumb/*",
    "/Images",
    "/Images/*",
    "/rProfileImage/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor sub
        CommandsMap.put("rImage", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("rThumb", 3);
        CommandsMap.put("Image", 4);
        CommandsMap.put("rProfileImage", 5);
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
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
                DisplayImage(Convertors.DISPLAY_IMAGE,args[2], args[3], request, response);
                break;
            case 2:
            	if (isJson)
            		imageListjson(LoggedInUser,args[2], args[3], request, response);
            	else
            		imageListjsp(LoggedInUser,args[2], args[3], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2], args[3], request, response);
                break;
            case 4:
            	ImagePage(Convertors.DISPLAY_IMAGE,args[2],args[3],  request, response);
                break;
            case 5:
            	DisplayImage(Convertors.DISPLAY_PROFILE,args[2], "", request, response);
                break;
            default:
                error("Bad Operator", response);
        }
    }

    /**
     /Instagrim/Folder/User
     */
    private void imageListjsp(String LoggedInUser, String User, String Folder, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("User", User);
        request.setAttribute("Folder", Folder);
        rd.forward(request, response);

    }   
    /**
    /Instagrim/Folder/User/json
    */
    private void imageListjson(String LoggedInUser, String User, String Folder, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User, Folder);


        AuthenticateImage ai = new AuthenticateImage();
        ai.setCluster(cluster);
        lsPics = ai.RemoveNotAuthorized(LoggedInUser,lsPics);
        
        String jasonResponse = JsonRenderer.render(lsPics);
       
        response.setContentType("application/json");
		response.setHeader("Content-Type", "application/json");  
		PrintWriter out = response.getWriter();
		out.write(jasonResponse);
    }

    
    
    
    private void ImagePage(int type, String User, String Image, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    	

       
        
        RequestDispatcher rd = request.getRequestDispatcher("/ImageDisplay.jsp");
        request.setAttribute("PicID", Image);
        request.setAttribute("Author", User);
        rd.forward(request, response);
    }
    
    private void DisplayImage(int type,String User,String Image, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	
    	
    	PicModel tm = new PicModel();
        tm.setCluster(cluster);
        Pic p;
        if(Convertors.DISPLAY_PROFILE != type)
        	p = tm.getPic(type,java.util.UUID.fromString(Image));
        else
        	p = tm.getPic(type,User);
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Folder = request.getParameter("Folder");
        int Accessability = 0;
        try
        {
        	Accessability = Integer.parseInt(request.getParameter("Accessability"));
        }
        catch(Exception e)
        {
        	Accessability = 0;
        }
        
    	for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username="majed";
            if (lg.getlogedin()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, Folder, username, Accessability);

                is.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
             rd.forward(request, response);
        }

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
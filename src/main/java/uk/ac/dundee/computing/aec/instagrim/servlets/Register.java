/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.UserModel;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }




    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String password=request.getParameter("password");
    	
    	
    	UserProfile new_userprofile = new UserProfile();
    	
    	new_userprofile.setUsername(request.getParameter("username"));        
    	new_userprofile.setFirstName(request.getParameter("firstname"));
    	new_userprofile.setLastName(request.getParameter("lastname"));
    	String dob = request.getParameter("dobyear") + "." + request.getParameter("dobmounth") + "." + request.getParameter("dobday");
    	try {
			new_userprofile.setDob(new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH).parse(dob));
		} catch (ParseException e) 
		{
			e.printStackTrace();
		}

        
        UserModel us=new UserModel();
        us.setCluster(cluster);
        us.RegisterUser(new_userprofile, password);
        
	response.sendRedirect("/Instagrim");
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

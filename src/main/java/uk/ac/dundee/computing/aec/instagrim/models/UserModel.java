/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_GRAYSCALE;
import static org.imgscalr.Scalr.pad;
import static org.imgscalr.Scalr.resize;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;

import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

/**
 *
 * @author Administrator
 */
public class UserModel {
    Cluster cluster;
    public UserModel(){
        
    }
    
    public UserProfile getUserprofile(String username)
    {
    	UserProfile userprofile = new UserProfile();
    	Session session = cluster.connect("instagrimTS");
    	PreparedStatement ps = session.prepare("select * from userprofiles where login = ?");
    	BoundStatement boundStatement = new BoundStatement(ps);
    	ResultSet rs = session.execute( boundStatement.bind(username));
    	Row row = rs.one();
    	userprofile.setUsername(row.getString("login"));
    	userprofile.setUsername(row.getString("login"));
    	userprofile.setFirstName(row.getString("firstname"));	
    	userprofile.setLastName(row.getString("lastname"));	
    	userprofile.setDob(row.getDate("dob"));	
    	PicModel tmppicmodel =new PicModel();
    	tmppicmodel.setCluster(cluster);
    	userprofile.setPicnumber(12);//tmppicmodel.getPicsForUser(username).size());	

    	session.close();
    	return userprofile;
    }
    

    
    public void MakeFriend(String user1, String user2)
    {
    	Session session = cluster.connect("instagrimTS");
    	PreparedStatement ps = session.prepare("insert into friends (user1, user2) Values(?,?)");
    	ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
    	rs = session.execute( 
                boundStatement.bind( 
                		user1, user2));
    	session.close();
    }
    
    public java.util.LinkedList<UserProfile> getAllUserprofiles()
    {
    	java.util.LinkedList<UserProfile> userprofiles = new java.util.LinkedList<UserProfile>();
    	Session session = cluster.connect("instagrimTS");
    	PreparedStatement ps = session.prepare("select login, dob, firstname, lastname from userprofiles");
    	BoundStatement boundStatement = new BoundStatement(ps);
    	ResultSet rs = session.execute( boundStatement);

    	if (rs.isExhausted()) 
        {
            System.out.println("No User");
            return null;
        } 
    	
    	for (Row row : rs) 
        {
    		UserProfile tmp = new UserProfile();
    		tmp.setUsername(row.getString("login"));
    		tmp.setUsername(row.getString("login"));
    		tmp.setFirstName(row.getString("firstname"));	
    		tmp.setLastName(row.getString("lastname"));	
    		tmp.setDob(row.getDate("dob"));	
    		userprofiles.add(tmp);
	    }
    	session.close();
    	return userprofiles;
    }
    
    public boolean RegisterUser(byte[] b,String type, UserProfile userprofile, String password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrimTS");
        
       System.out.println("ITT1");
       
       //System.out.println("ITT2" + b.length + "---" + b2.length );
       byte[] b2;
       ByteBuffer buffer = null;
     
	       BufferedImage img = createProfilePic(Convertors.createImageFromBytes(b));  
	       if (img != null)
	       {
	    	  b2 = Convertors.createBytesFromImage(img, type);	       
	    	  buffer = ByteBuffer.wrap(b2);
	       }
	       else
	       {
	    	   b2 = new byte[0];
	    	   buffer = ByteBuffer.allocate(0);
	       }
      
        
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,firstname,lastname,dob, profilepic, piclength, type) Values(?,?,?,?,?,?,?,?)");
       
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		userprofile.getUsername(),EncodedPassword, userprofile.getFirstName(), userprofile.getLastName(), userprofile.getDob(), buffer, b2.length, type));
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
        return true;
    }
    
    public BufferedImage createProfilePic(BufferedImage img)
    {
    	if (img == null)
    		return null;
    	/*
    	img = (BufferedImage) img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        return pad(img, 2);*/
        
    	int old_width = img.getWidth();  
        int old_height = img.getHeight();  
          
        BufferedImage newbi = new BufferedImage(200, 200, img.getType());  
        Graphics2D g = newbi.createGraphics();  
        /*
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        */
        g.drawImage(img, 0, 0, 200, 200, 0, 0, old_width, old_height, null);  
        g.dispose();  
        
 
        return newbi;  
        
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                
                if (StoredPass.compareTo(EncodedPassword) == 0)
                {
                	session.close();
                    return true;
                }
            }
        }
   
        
        session.close();
    return false;  
    }
    
    

    
    
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
}

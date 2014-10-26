package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.SComment;
import uk.ac.dundee.computing.aec.instagrim.stores.SFolder;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    
	public void PostAComment(String Comment,java.util.UUID UUID, String User)
	{
		Convertors convertor = new Convertors();
		java.util.UUID comid = convertor.getTimeUUID();
		 Session session = cluster.connect("instagrimTS");
	     PreparedStatement ps = session.prepare("insert into piccomments (picid,commentid,user,comment) Values(?,?,?,?)");
	       
	     BoundStatement boundStatement = new BoundStatement(ps);
	     session.execute( 
	             boundStatement.bind( 
	            		 UUID, comid ,User, Comment));  
	     session.close();
	}
	
	public java.util.LinkedList<SComment> getPicComments(java.util.UUID PicID)
	{
    	java.util.LinkedList<SComment> comments = new java.util.LinkedList<SComment>();

    	
    	Session session = cluster.connect("instagrimTS");
        ResultSet rs = null;
        PreparedStatement ps = session.prepare("select user,comment,commentid from piccomments where picid =?");
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		PicID));
        if (rs.isExhausted()) 
        {
            System.out.println("No Comment");
            return null;
        } 
        else 
        {
            for (Row row : rs) 
            {
            	SComment tmp = new SComment();
            	tmp.setComment(PicID, row.getUUID("commentid"), row.getString("user"), row.getString("comment"));
            	comments.add(tmp);
            	
            }
        }
        
        session.close();
        return comments;
	}
	
    public void insertPic(byte[] b, String type, String name, String folder, String user, int Accessability) {
        
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
     
            
            BufferedImage img = Convertors.createImageFromBytes(b);  
            
            
            
            
            
            byte []  thumbb = picresize(img,types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(img,types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrimTS");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, picidindex, user,folder,accessability, pic_added) values(?,?,?,?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid,picid, user, folder, Accessability, DateAdded));
            session.close();

        
    }

    public byte[] picresize(BufferedImage img,String type) 
    {

            BufferedImage thumbnail = createThumbnail(img);
            byte[] imageInByte = Convertors.createBytesFromImage(thumbnail, type);
            return imageInByte;
    }
    
    public byte[] picdecolour(BufferedImage img,String type)
    {
            BufferedImage processed = createProcessed(img);
            byte[] imageInByte = Convertors.createBytesFromImage(img, type);
            return imageInByte;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
   
    public java.util.LinkedList<SFolder> getUserPicsFolders(String User)
    {
    	java.util.LinkedList<SFolder> folders = new java.util.LinkedList<SFolder>();

    	
    	Session session = cluster.connect("instagrimTS");
        ResultSet rs = null;
        PreparedStatement ps = session.prepare("select user,folder from userpiclist where user =?");
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) 
        {
            System.out.println("No Folder");
            return null;
        } 
        else 
        {
            for (Row row : rs) 
            {
            	SFolder tmp = new SFolder();
            	tmp.setFolder(row.getString("folder"), User);
            	
            	boolean inExist = false;
            	for(int i = 0; i < folders.size(); i++)
            	{
            		if (folders.get(i).getFolderName().equals(tmp.getFolderName()))
            		{
            			inExist = true;
            			break;
            		}
            	}
            	if (!inExist)
            		folders.add(tmp);
            }
        }
        
        session.close();
        return folders;
             
    }
    
    
    public int getPicAccessabilityByID(java.util.UUID id)
    {
    	Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select accessability from userpiclist where picidindex = ?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( 
                boundStatement.bind(
                		id));
        
        int accessability = 0;
        for (Row row :rs)
        	accessability = row.getInt("accessability");
        session.close();
        return accessability;
        
    }
    
    
    public int getPicAccessability(Pic pic)
    {
    	Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select picid, accessability from userpiclist where user =? AND folder=? ALLOW FILTERING");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( 
                boundStatement.bind(
                		pic.getPicAuthor(), pic.getFolder()));
        
        int accessability = 0;
        for (Row row :rs)
        	accessability = row.getInt("accessability");
        session.close();
        return accessability;
        
    }
    
    public java.util.LinkedList<Pic> getPicsForUser(String User, String Folder) {
    	

        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select picid, accessability from userpiclist where user =? AND folder=? ALLOW FILTERING");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User, Folder));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                pic.setPicAuthor(User);
                pic.setAccessability(row.getInt("accessability"));
                Pics.add(pic);

            }
        }
        session.close();
        return Pics;
    }
    
    public Pic getPic(int image_type, java.util.UUID picid) {
    	return getPic( image_type, picid, "");
    }
    
    public Pic getPic(int image_type, String User) {
    	return getPic( image_type, null, User);
    }

    public Pic getPic(int image_type, java.util.UUID picid, String User) {
    	
    	
        Session session = cluster.connect("instagrimTS");
        ByteBuffer bImage = null;
        String type = null;
       
        int length = 0;
        String folder = "";
        String author = "";

        try {
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type,user from pics where picid =? ALLOW FILTERING");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type,user from pics where picid =? ALLOW FILTERING");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type,user from pics where picid =? ALLOW FILTERING");
            } else if (image_type == Convertors.DISPLAY_PROFILE) {
                ps = session.prepare("select profilepic,piclength,type from userprofiles where login =? ALLOW FILTERING");
            }
            
            if (image_type != Convertors.DISPLAY_PROFILE)
            {
	            BoundStatement boundStatement = new BoundStatement(ps);
	            rs = session.execute( // this is where the query is executed
	                    boundStatement.bind( // here you are binding the 'boundStatement'
	                            picid));
            }
            else
            {
	            BoundStatement boundStatement = new BoundStatement(ps);
	            rs = session.execute( // this is where the query is executed
	                    boundStatement.bind( // here you are binding the 'boundStatement'
	                            User));
            }

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                        author = row.getString("user");
                        
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                        author = row.getString("user");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                        author = row.getString("user");
              
                    }
                    else if (image_type == Convertors.DISPLAY_PROFILE) {
                        bImage = row.getBytes("profilepic");
                        length = row.getInt("piclength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type, folder, User);

        return p;

    }
    
    


}

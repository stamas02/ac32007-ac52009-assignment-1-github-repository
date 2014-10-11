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
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
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
		 Session session = cluster.connect("instagrim");
	     PreparedStatement ps = session.prepare("insert into piccomments (picid,commentid,user,comment) Values(?,?,?,?)");
	       
	     BoundStatement boundStatement = new BoundStatement(ps);
	     session.execute( 
	             boundStatement.bind( 
	            		 UUID, comid ,User, Comment));        
	}
	
	public java.util.LinkedList<Comment> getPicComments(java.util.UUID PicID)
	{
    	java.util.LinkedList<Comment> comments = new java.util.LinkedList<Comment>();

    	
    	Session session = cluster.connect("instagrim");
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
            	Comment tmp = new Comment();
            	tmp.setComment(PicID, row.getUUID("commentid"), row.getString("user"), row.getString("comment"));
            	comments.add(tmp);
            	
            }
        }
        
        
        return comments;
	}
	
    public void insertPic(byte[] b, String type, String name, String folder, String user) {
        try {
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user,folder, pic_added) values(?,?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, folder, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
   
    public java.util.LinkedList<String> getUserPicsFolders(String User)
    {
    	java.util.LinkedList<String> folders = new java.util.LinkedList<String>();

    	
    	Session session = cluster.connect("instagrim");
        ResultSet rs = null;
        PreparedStatement ps = session.prepare("select folder from userpiclist where user =?");
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
            	folders.add( row.getString("folder"));
        }
        
        
        return folders;
             
    }
    
    
    public java.util.LinkedList<Pic> getPicsForUser(String User, String Folder) {
    	

        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =? AND folder=? ALLOW FILTERING");
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
                Pics.add(pic);

            }
        }
        return Pics;
    }
    
    public Pic getPic(int image_type, java.util.UUID picid) {
    	return getPic( image_type, picid, "");
    }
    
    public Pic getPic(int image_type, String User) {
    	return getPic( image_type, null, User);
    }

    public Pic getPic(int image_type, java.util.UUID picid, String User) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        String folder = "";
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type,folder from pics where picid =? ALLOW FILTERING");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =? ALLOW FILTERING");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,piclength,type from pics where picid =? ALLOW FILTERING");
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
                        folder = row.getString("folder");
                        
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                        
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                        
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
        p.setPic(bImage, length, type, folder);

        return p;

    }
    
    


}

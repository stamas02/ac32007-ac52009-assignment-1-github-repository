package uk.ac.dundee.computing.aec.instagrim.models;

import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class FriendModel {
	Cluster cluster;
	
	public FriendModel()
	{
		
	}
	
	public void AcceptFriendRequest(String user1, String user2)
	{
		 Convertors convertor = new Convertors();
		
		
	   	Session session = cluster.connect("instagrimTS");
	   	PreparedStatement ps = session.prepare("insert into friends (user1, user2) Values(?,?)");
		PreparedStatement ps2 = session.prepare("insert into friends (user1, user2) Values(?,?)");
	   	ResultSet rs = null;
		ResultSet rs2 = null;
	    BoundStatement boundStatement = new BoundStatement(ps);
	    BoundStatement boundStatement2 = new BoundStatement(ps2);
	   	rs = session.execute( boundStatement.bind(user1, user2));
	   	rs2 = session.execute( boundStatement2.bind(user2, user1));
	   	
	   	MessageModel messagemodel = new MessageModel();
	   	messagemodel.setCluster(cluster);
	   	messagemodel.DeleteFrendRequestMessage(user2, user1);
	   	session.close();
	}
	
    public boolean IsFriend(String user1, String user2)
    {
    	Session session = cluster.connect("instagrimTS");
    	PreparedStatement ps = session.prepare("select * from friends where user1 = ? AND user2 = ?");
    	ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
    	rs = session.execute( 
                boundStatement.bind( 
                		user1, user2));
    	session.close();
    	if (rs.isExhausted()) 
            return false;
        
    	return true;
    	
    }
    
	public java.util.LinkedList<UserProfile> getFriendList(String user1)
	{
			java.util.LinkedList<UserProfile> userprofiles = new java.util.LinkedList<UserProfile>();
			Session session = cluster.connect("instagrimTS");
	        PreparedStatement ps = session.prepare("select user1, user2 from friends where user1 = ?");
	        ResultSet rs = null;
	        BoundStatement boundStatement = new BoundStatement(ps);
	        rs = session.execute( // this is where the query is executed
	                boundStatement.bind( // here you are binding the 'boundStatement'
	                		user1));
	        if (rs.isExhausted()) {
	            return null;
	        }
	        
	        UserModel usermodel = new UserModel();
	        usermodel.setCluster(cluster);
	        
	    	for (Row row : rs) 
	        {
	    		UserProfile tmp = new UserProfile();
	    		userprofiles.add(usermodel.getUserprofile(row.getString("user2")));

		    }
	    	session.close();
	        return userprofiles;
	}
	
	
	
	
	
	public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

}

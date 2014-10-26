package uk.ac.dundee.computing.aec.instagrim.models;







import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.UserProfile;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class MessageModel 
{
	Cluster cluster;
	
	public MessageModel()
	{
		
	}
	
	public void sendFriendRequest(String user1, String user2)
	{
		 Convertors convertor = new Convertors();
		java.util.UUID messageid = convertor.getTimeUUID();
		
    	Session session = cluster.connect("instagrimTS");
    	PreparedStatement ps = session.prepare("insert into messages (fromuser, touser,messagetype,messageid,message) Values(?,?,?,?,?)");
    	ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
    	rs = session.execute( 
                boundStatement.bind( 
                		user1, user2, 0, messageid, "I waana add you as my friend"));
    	session.close();
	}
	
	public java.util.LinkedList<UserProfile> getFriendRequests(String user)
	{
		java.util.LinkedList<UserProfile> userprofiles = new java.util.LinkedList<UserProfile>();
		
		Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select fromuser from messages where messagetype = ? AND touser = ?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		0,user));
        if (rs.isExhausted()) {
            return null;
        }
        
        for (Row row : rs) 
        {
    		UserProfile tmp = new UserProfile();
    		tmp.setUsername(row.getString("fromuser"));
    		userprofiles.add(tmp);

	    }
        session.close();
        return userprofiles;
	}
	
	public boolean isFriendRequestSent(String user1, String user2)
	{
		Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("select message from messages where messagetype = ? AND touser = ? AND fromuser =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		0, user2,user1));
        if (rs.isExhausted()) {
            return false;
        }
        session.close();
        return true;
	}
	
	public void DeleteFrendRequestMessage(String user1, String user2)
	{
		Session session = cluster.connect("instagrimTS");
        PreparedStatement ps = session.prepare("Delete from messages where messagetype = ? AND touser = ? AND fromuser =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                		0, user2,user1));
        session.close();
	}
	
	public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}

package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.Iterator;

import uk.ac.dundee.computing.aec.instagrim.models.FriendModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import com.datastax.driver.core.Cluster;


public class AuthenticateImage 
{

	Cluster cluster;
	
	public AuthenticateImage()
	{
		
	}
	
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	
	public boolean authenticate(String LoggdInUser,String User, String UUID)
	{
    	FriendModel friedmodel = new FriendModel();
    	friedmodel.setCluster(cluster);
		boolean friend = friedmodel.IsFriend(LoggdInUser, User);
		
		PicModel picmodel = new PicModel();
		picmodel.setCluster(cluster);
		
		int accessability = picmodel.getPicAccessabilityByID(java.util.UUID.fromString(UUID));
	
		
		return IsAuthorizedImage(LoggdInUser,User,friend,accessability);
	}
	
    private boolean IsAuthorizedImage (String LoggdInUser, String User, boolean friend, int accessability)
    {

    	if (LoggdInUser.equals(User))
    		return true;
    	else if (accessability == 0)
			return false;
		else if ((accessability == 1)&&(!friend))
			return false;
		else
			return true;
    }
    
    public java.util.LinkedList<Pic> RemoveNotAuthorized(String LoggdInUser, java.util.LinkedList<Pic> lspics )
    {
    	String User = lspics.get(0).getPicAuthor();
    	
    	FriendModel friedmodel = new FriendModel();
    	friedmodel.setCluster(cluster);
		boolean friend = friedmodel.IsFriend(LoggdInUser, User);
			
		
    	Iterator<Pic> it = lspics.iterator();
    	while (it.hasNext())
    	{  		
    		Pic tmp = it.next();
    		System.out.println(LoggdInUser);
    		System.out.println(User);
    		System.out.println(tmp.getAccessability());
    		if(tmp.getPicAuthor() != User)
    			return null;
    		if (!IsAuthorizedImage(LoggdInUser,User,friend,tmp.getAccessability()))   			
    			it.remove();
    	}
    	
    	return lspics;
    	
    }

}

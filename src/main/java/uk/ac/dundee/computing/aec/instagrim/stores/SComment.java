package uk.ac.dundee.computing.aec.instagrim.stores;

public class SComment 
{
	private java.util.UUID picID=null;
	private java.util.UUID CommentID=null;
	private String User;
	private String Comment;
	
	public void setPicID(java.util.UUID picID)
	{
		this.picID = picID;
	}
	public java.util.UUID getPicID()
	{
		return picID;
	}
	
	public void setCommentID(java.util.UUID CommentID)
	{
		this.CommentID = CommentID;
	}
	public java.util.UUID getCommentID()
	{
		return CommentID;
	}
	
	public void setUser(String User)
	{
		this.User = User;
	}
	public String getUser()
	{
		return this.User;
	}
	
	public void setComment(String Comment)
	{
		this.Comment = Comment;
	}
	public String getComment()
	{
		return Comment;
	}
	
	public void setComment(java.util.UUID picID, java.util.UUID CommentID, String User, String Comment)
	{
		setPicID(picID);
		setCommentID(CommentID);
		setComment(Comment);
		setUser(User);
	}

}

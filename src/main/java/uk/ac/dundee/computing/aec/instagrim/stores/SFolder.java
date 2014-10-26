package uk.ac.dundee.computing.aec.instagrim.stores;

public class SFolder 
{
	String foldername;
	String author;
	
	public SFolder()
	{
	
	}
	
	public void setFolderName(String foldername)
	{
		this.foldername = foldername;
	}
	
	public String getFolderName()
	{
		return this.foldername;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	public String getAuthor()
	{
		return this.author;
	}
	
	public void setFolder(String foldername, String author)
	{
		 setFolderName(foldername);
		 setAuthor(author);
	}
	
}

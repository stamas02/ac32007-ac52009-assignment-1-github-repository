package uk.ac.dundee.computing.aec.instagrim.stores;


import java.util.Date;



public class UserProfile {
	private String username;
	private String firstName;
	private String lastName;
	private Date dob;
	private int picNumber;
	
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public Date getDob()
	{
		return this.dob;
	}
	
	public void setDob(Date dob)
	{
		this.dob = dob;
	}
	public int getPicnumber()
	{
		return this.picNumber;	
	}
	public void setPicnumber(int picNumber)
	{
		this.picNumber = picNumber;
	}
}

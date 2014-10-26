package uk.ac.dundee.computing.aec.instagrim.lib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

public class LoginChecker 
{
	
	static public boolean ValidateSession(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (session == null )
			return false;
		
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		if (lg == null)
			return false;
		
		return lg.getlogedin();
	}
	
	static public String LoggedInUser(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (session == null )
			return "";
		
		LoggedIn lg = (LoggedIn) (session.getAttribute("LoggedIn"));
		if (lg == null)
			return "";
		
		return lg.getUsername();
	}

}

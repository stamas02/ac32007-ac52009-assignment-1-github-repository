<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <header>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        <nav>
            <ul>
                
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
       
        <article>
            <h3>Register as user</h3>
            <form method="POST"  action="Register">
                <ul>
                    <li>User Name <input type="text" name="username"></li>
                    <li>Password <input type="password" name="password"></li>
                    <li>First name <input type="text" name="firstname"></li>
                    <li>Last name <input type="text" name="lastname"></li>
                    <li>
                    	<select name="dobday">
                    	<%
                    		for(int i = 0; i < 31; i++)
                    		{
                    			String dd = String.valueOf(i+1);
                    			if (i <9)
                    				dd = "0" + dd;
                    			out.println("<option value='" + (dd)+ "'>" + (dd) + "</option>" );
                    		}
                    	%> 
						</select>
						<select name="dobmounth">
                    	<%
                    	DateFormatSymbols dfs = new DateFormatSymbols();
                        String[] months = dfs.getMonths();
                    		for(int i = 0; i < 12; i++ )
                    			out.println("<option value='" + (i+1) + "'>" + months[i] + "</option>" );
                    	%> 
						</select>
						<select name="dobyear">
                    	<%
                    		for(int i = Calendar.getInstance().get(Calendar.YEAR)-12 ; i > 1900; i--)
                    			out.println("<option value='" + i+ "'>" + i + "</option>" );
                    	%> 
						</select>
						
					</li>
                </ul>
                <br/>
                <input type="submit" value="Regidter"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>

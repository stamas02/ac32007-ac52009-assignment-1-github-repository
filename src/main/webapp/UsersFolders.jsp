<%-- 
    Document   : UsersFolders
    Created on : Oct 07, 2014, 16:23:11 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
        <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script>
    </head>
    <body>
    <li><a href="/Instagrim/Logout">Log Out</a></li>
        <%
            String User = (String)request.getAttribute("User");
        %>
        <script>
        $( document ).ready(function() {
        	var imageListjason = $.getJSON( "/Instagrim/Folder/<%= User %>/json", function(frequests) {
        		if (frequests != "")
        		{
        			for (var i = 0; i < frequests.Data.length; i++) { 
        				$('#folderlist').append("<a href='/Instagrim/Images/" + frequests.Data[i].Author + "/" + frequests.Data[i].FolderName + "' >" + frequests.Data[i].FolderName  + "</a><br/>");
        			}
        		}
        	});
			
		});
        </script>
        <header>
        
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/upload.jsp">Create Folder</a></li>
                
            </ul>
        </nav>
 
        <article>
            <h1>Your Folders</h1>
			<div id="folderlist">
			</div>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>

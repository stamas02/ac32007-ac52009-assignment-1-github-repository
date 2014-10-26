<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/Styles.css" />
        <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script></head>
    </head>
    <body>
    <li><a href="<%=request.getContextPath()%>/Logout">Log Out</a></li>
        <%
            String User = (String)request.getAttribute("User");
            String Folder = (String)request.getAttribute("Folder");
        %>
        <script>
        $( document ).ready(function() {
        	var imageListjason = $.getJSON( "<%=request.getContextPath()%>/Images/<%= User %>/<%= Folder %>/json", function(frequests) {
        		if (frequests != "")
        		{
        			for (var i = 0; i < frequests.Data.length; i++) { 
        				$('#piccontainer').append('<div style="background-color=blue"><a href="<%=request.getContextPath()%>/Image/' + frequests.Data[i].PicAuthor + '/' + frequests.Data[i].SUUID + '"><img src="<%=request.getContextPath()%>/rThumb/' + frequests.Data[i].PicAuthor + '/' + frequests.Data[i].SUUID + '"/></a></div>');
        			}
        		}
        	});
			
		});
        </script>  
        
        <header>
        
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
		
		<div id="piccontainer">
		</div>
			 

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="<%=request.getContextPath()%>">Home</a></li>
            </ul>
        </footer>
    </body>
</html>

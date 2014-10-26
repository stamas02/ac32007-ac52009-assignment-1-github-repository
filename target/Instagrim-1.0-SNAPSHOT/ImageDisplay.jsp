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
         <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script>
    </head>
    
    <body>
    <li><a href="<%=request.getContextPath()%>/Logout">Log Out</a></li>
	    <%
		String PicID = (String) request.getAttribute("PicID");
		String Author =   (String) request.getAttribute("Author");
	    %>
        <script>
        $( document ).ready(function() {
        	var imageListjason = $.getJSON( "<%=request.getContextPath()%>/Comment/<%= Author %>/<%= PicID %>/json", function(frequests) {
        		if (frequests != "")
        		{
        			for (var i = 0; i < frequests.Data.length; i++) { 
        				$('#commentlist').append(frequests.Data[i].User + "<br><br>"+ frequests.Data[i].Comment + "<br><br><br>");
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
                <li class="nav"><a href="<%=request.getContextPath()%>/upload.jsp">Upload</a></li>
            </ul>
        </nav>
 
        <article>
            <h1>Your Pics</h1>
        <%

                    java.util.LinkedList<SComment> comments = (java.util.LinkedList<SComment>) request.getAttribute("Comments");
        if (PicID == null) {
        %>
        <p>No Pictures found</p>
        <%
        	} else {
        %>
       <img src="<%=request.getContextPath()%>/rImage/<%=Author%>/<%=PicID%>"><br/><%
       	}
       %>
        </article>
        
		<div id="commentlist">
		</div>
       
        
        <form action="<%=request.getContextPath()%>/Comment" method="POST">
        	<input type="hidden" name="PicID" value="<%=PicID%>" />
        	<input type="hidden" name="User" value="<%=Author%>" />
        	<input type="text" name="Comment" />
        	<input type="submit" value="POST" />
        </form>
    </body>
</html>

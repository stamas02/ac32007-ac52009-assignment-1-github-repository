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
    </head>
    <body>
        <header>
        
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
            <ul>
                <li class="nav"><a href="/Instagrim/upload.jsp">Create Folder</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
 
        <article>
            <h1>Your Folders</h1>
        <%
            java.util.LinkedList<String> lsFolders = (java.util.LinkedList<String>) request.getAttribute("Folders");
            if (lsFolders == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<String> iterator;
            iterator = lsFolders.iterator();
            while (iterator.hasNext()) {
                String p = (String) iterator.next();

        %>
        <a href="/Instagrim/Images/<%=p%>" ><% out.println(p); %></a><br/><%

            }
            }
        %>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>

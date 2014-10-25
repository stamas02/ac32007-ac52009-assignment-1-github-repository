<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script>
    </head>
    <body>
    <li><a href="/Instagrim/Logout">Log Out</a></li>
    
        <%
        	LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        	String User ="";
        	if (lg != null) 
            	User = lg.getUsername();
        %>
        <script>
        $( document ).ready(function() {
        	var imageListjason = $.getJSON( "/Instagrim/Folder/<%= User %>/json", function(frequests) {
        		if (frequests != "")
        		{
        			for (var i = 0; i < frequests.Data.length; i++) { 
        				$('#folderlist').append("<option value='"+ frequests.Data[i].FolderName + "' >" + frequests.Data[i].FolderName  + "</option><br/>");
        			}
        		}
        	});


		});
        

        </script>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        <nav>
            <ul>
                <li class="nav"><a href="upload.jsp">Upload</a></li>
            </ul>
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile"><br/>
				Folder <input id="selectedFolder" type="text" value="Default" name="Folder" />
				<select id = "folderlist" onChange="updateSelected()">

				</select>
				
				<br/>
				<select name = "Accessability">
				  <option value="0">Private</option>
				  <option value="1">Friends</option>
				  <option value="2">Public</option>
				</select>
                <br/>
                <input type="submit" value="Press"> to upload the file!
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>


<script>
function updateSelected()
{
	var flist = document.getElementById("folderlist");
	var sfolder = document.getElementById("selectedFolder");
	sfolder.value = flist.options[flist.selectedIndex].value;
}
</script>

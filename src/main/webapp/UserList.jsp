
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/Styles.css" />
        <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script>
    </head>
    <body>
    <li><a href="<%=request.getContextPath()%>/Logout">Log Out</a></li>
        <script>
        $( document ).ready(function() {
        	var imageListjason = $.getJSON( "<%=request.getContextPath()%>/Users/json", function(frequests) {
        		if (frequests != "")
        		{
        			for (var i = 0; i < frequests.Data.length; i++) { 
        				var link_to_profile="<%=request.getContextPath()%>/Profile/" + frequests.Data[i].Username;
        				var fname = frequests.Data[i].FirstName;
        				var lname = frequests.Data[i].LastName;
        				var profilepic = "<%=request.getContextPath()%>/rProfileImage/"+ frequests.Data[i].Username;
        				$('#userscontainer').append("<img src='<%=request.getContextPath()%>/rProfileImage/" + frequests.Data[i].Username + "' />");
        				$('#userscontainer').append("<a href='" + link_to_profile + "'><div style='background-color:#E6E6FA'>First name: " +fname+ "<br/> Lastname: " + lname +" </div>");
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
                <li class="nav"><a href="<%=request.getContextPath()%>/upload.jsp">Create Folder</a></li>
            </ul>
        </nav>
 
        <article>
			<div id="userscontainer">
			</div>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="<%=request.getContextPath()%>">Home</a></li>
            </ul>
        </footer>
    </body>
</html>

</body>
</html>
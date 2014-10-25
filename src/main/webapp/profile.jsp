<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.InputStream" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.11.1.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>



<body>
<li><a href="/Instagrim/Logout">Log Out</a></li>

<%  String User = (String) request.getAttribute("User"); %>

<script>
$( document ).ready(function() {
	var User = "<%= User %>";
	
	var jqxhr = $.getJSON( "/Instagrim/Profile/"+ User + "/json", function(frequests) {
		if (frequests != "") // Probably some error :(
			$('#userprofile').append( "'<img src='/Instagrim/rProfileImage/" + User + "'/><br/>" ); 
			$('#userprofile').append("Username: " +  frequests.Username + "<br/>");
			$('#userprofile').append("Firstname: " +  frequests.FirstName + "<br/>"); 	
			$('#userprofile').append("Lastname: " +  frequests.LastName + "<br/>"); 	
			$('#userprofile').append("DOB: " +  frequests.Dob + "<br/>"); 
			$('#userprofile').append("Pic number: " +  frequests.Picnumber + "<br/>"); 	
		});
		
	var jqxhr = $.getJSON( "/Instagrim/Friend/"+ User + "/json", function(frequests) {
		if(frequests.friendstatus==0)
			$('#friendStatus').append("<a href='/Instagrim/FriendRequest/" + User + "'>Send Friend Request</a>");
		else if (frequests.friendstatus==1)
			$('#friendStatus').append("This user is already your friend");
		});
		
	var jqxhr = $.getJSON( "/Instagrim/Requests/" + User + "/json", function(frequests) {
		if (frequests != "")
		{
			for (var i = 0; i < frequests.Data.length; i++) { 
				$('#friendRequests').append("<a href='/Instagrim/AcceptFriend/" + frequests.Data[i].Username + "' >" + frequests.Data[i].Username + "</a><br>");
			}
		}
	});
	
	var jqxhr = $.getJSON( "/Instagrim/Friends/" + User + "/json", function(frequests) {
		if (frequests != "")
		{
			for (var i = 0; i < frequests.Data.length; i++) { 
				$('#friendlist').append("<a href='/Instagrim/Profile/" + frequests.Data[i].Username + "' >" + frequests.Data[i].Username + "</a><br>");
			}
		}
	});
});
</script>
		
		<li><a href="/Instagrim/Folder/<%=User%>">Images</a></li>
		
		<div id="friendStatus">
		</div>
		<div id ="userprofile"> 
		</div>
		
		<div id="friendRequests" style="background-color: red;">
		Friend Requests: <br>
		</div>
		<div id="friendlist" style="background-color: green;">
		Your Friends: <br>
		</div>
		

</body>
</html>
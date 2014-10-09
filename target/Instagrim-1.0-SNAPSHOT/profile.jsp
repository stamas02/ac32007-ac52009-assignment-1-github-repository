<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
		<%  UserProfile userprofile = (UserProfile) request.getAttribute("userprofile"); %>
		Username: <% out.println(userprofile.getUsername()); %>
		First name:<% out.println(userprofile.getFirstName()); %>
		Last name:<% out.println(userprofile.getLastName()); %>
		DOB: <% out.println(userprofile.getDob()); %>
		Picture number: <% out.println(userprofile.getPicnumber()); %>
</body>
</html>
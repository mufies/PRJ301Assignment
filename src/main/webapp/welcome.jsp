<%--
  Created by IntelliJ IDEA.
  User: mufi
  Date: 6/13/2025
  Time: 10:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Welcome</h1>

<%
    String jwt = (String) session.getAttribute("jwt");
    if (jwt != null) {
%>
    <p>Your JWT: <%= jwt %></p>
<%
    }
%>
</body>
</html>

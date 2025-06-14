
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="login">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required /><br />

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required /><br />

    <input type="submit" value="Login" />

    <a href="register">Register</a>

</form>
<%-- Display error message if login fails --%>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null && !errorMessage.isEmpty()) {
%>
    <p style="color: red;"><%= errorMessage %></p>
<%
    }
%>

</body>
</html>

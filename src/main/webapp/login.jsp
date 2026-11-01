<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest</div>
        <ul>
            <li><a href="index.html">Home</a></li>
            <li><a href="register.jsp">Register</a></li>
        </ul>
    </nav>

    <div class="form-box">
        <h2>Student Login</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
            if ("registered".equals(request.getParameter("msg"))) {
        %>
            <div class="msg-success">Registration successful! Please log in.</div>
        <%
            }
        %>

        <form action="login" method="post">
            <div class="form-group">
                <label>Username or Email</label>
                <input type="text" name="loginId" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn" style="width:100%">Login</button>
        </form>
        <p style="text-align:center; margin-top:15px;">
            New here? <a href="register.jsp" style="color:#00ffc8;">Register</a>
        </p>
    </div>
</body>
</html>
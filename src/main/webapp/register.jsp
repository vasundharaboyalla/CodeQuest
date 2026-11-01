<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest</div>
        <ul>
            <li><a href="index.html">Home</a></li>
            <li><a href="login.jsp">Login</a></li>
        </ul>
    </nav>

    <div class="form-box">
        <h2>Create Your Account</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
        %>

        <form action="register" method="post">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" required
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>">
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required minlength="4">
            </div>
            <div class="form-group">
                <label>Confirm Password</label>
                <input type="password" name="confirmPassword" required minlength="4">
            </div>
            <button type="submit" class="btn" style="width:100%">Register</button>
        </form>
        <p style="text-align:center; margin-top:15px;">
            Already have an account? <a href="login.jsp" style="color:#00ffc8;">Login</a>
        </p>
    </div>
</body>
</html>
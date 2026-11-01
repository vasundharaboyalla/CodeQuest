<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest</div>
        <ul>
            <li><a href="index.html">Home</a></li>
        </ul>
    </nav>

    <div class="form-box">
        <h2>Admin Login</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
        %>

        <form action="admin-login" method="post">
            <div class="form-group">
                <label>Admin Username</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="btn" style="width:100%">Login as Admin</button>
        </form>
    </div>
</body>
</html>
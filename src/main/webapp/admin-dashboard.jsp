<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("admin-login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest Admin</div>
        <ul>
            <li><a href="admin-dashboard.jsp">Dashboard</a></li>
            <li><a href="add-question.jsp">Add Question</a></li>
            <li><a href="view-questions">View Questions</a></li>
            <li><a href="admin-logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1 style="color:#00ffc8;">Welcome, <%= session.getAttribute("adminName") %>!</h1>
        <p style="color:#8080a0; margin-bottom:30px;">
            Role: <%= session.getAttribute("adminRole") %>
        </p>

        <div class="stats-grid">
            <div class="stat-box">
                <a href="add-question.jsp" class="btn">➕ Add New Question</a>
            </div>
            <div class="stat-box">
                <a href="view-questions" class="btn">📋 Manage Questions</a>
            </div>
        </div>
    </div>
</body>
</html>
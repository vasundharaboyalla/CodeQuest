<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="model.User" %>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    User user = (User) request.getAttribute("user");
    Integer completed = (Integer) request.getAttribute("completedCount");
    Integer xpInLevel = (Integer) request.getAttribute("xpInLevel");
    Integer xpToNext = (Integer) request.getAttribute("xpToNext");
    if (completed == null) completed = 0;
    if (xpInLevel == null) xpInLevel = 0;
    if (xpToNext == null) xpToNext = 100;
       int nextLevel = (user != null ? user.getLevel1() : 1) + 1;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest</div>
        <ul>
            <li><a href="dashboard">Dashboard</a></li>
            <li><a href="missions">Missions</a></li>
            <li><a href="leaderboard">Leaderboard</a></li>
            <li><a href="logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
        <h1 style="color:#00ffc8; margin-bottom:10px;">
            Welcome, <%= user != null ? user.getUsername() : session.getAttribute("username") %>! 👋
        </h1>
        <p style="color:#8080a0;">Keep learning, keep growing.</p>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
        %>

        <div class="stats-grid">
            <div class="stat-box">
                <div class="value"><%= user != null ? user.getXp() : 0 %></div>
                <div class="label">Total XP</div>
            </div>
            <div class="stat-box">
                <div class="value"><%= user != null ? user.getLevel() : 1 %></div>
                <div class="label">Current Level</div>
            </div>
            <div class="stat-box">
                <div class="value"><%= completed %></div>
                <div class="label">Missions Completed</div>
            </div>
        </div>

        <div class="card">
            <h3>Progress to Next Level</h3>
            <p style="color:#8080a0; margin-bottom:10px;">
                <%= xpInLevel %> / 100 XP — 
                <%= xpToNext %> XP to Level <%= nextLevel %>
            </p>
            <div class="progress-bar">
                <div class="progress-fill" style="width:<%= xpInLevel %>%"></div>
            </div>
            <div style="margin-top:25px;">
                <a href="missions" class="btn">🎯 Start a Mission</a>
                <a href="leaderboard" class="btn btn-secondary">🏆 View Leaderboard</a>
            </div>
        </div>
    </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String error = (String) request.getAttribute("error");
    String result = (String) request.getAttribute("result");
    Boolean isCorrect = (Boolean) request.getAttribute("isCorrect");
    String missionTitle = (String) request.getAttribute("missionTitle");
    String selected = (String) request.getAttribute("selectedAnswer");
    String correct = (String) request.getAttribute("correctAnswer");
    Integer xpReward = (Integer) request.getAttribute("xpReward");
    Integer newXp = (Integer) request.getAttribute("newXp");
    Integer newLevel = (Integer) request.getAttribute("newLevel");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mission Result - CodeQuest</title>
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
        <div class="card">
            <h2>Mission Result</h2>

            <% if (error != null) { %>
                <div class="msg-error"><%= error %></div>
            <% } else if ("duplicate".equals(result)) { %>
                <div class="msg-error">⚠️ You already attempted this mission.</div>
            <% } else if (isCorrect != null && isCorrect) { %>
                <div class="msg-success">
                    ✅ Correct! You earned <strong>+<%= xpReward %> XP</strong>!
                </div>
                <p style="color:#c0c0e0; margin-top:15px;">
                    Mission: <strong><%= missionTitle %></strong>
                </p>
                <p style="color:#00ffc8; margin-top:15px;">
                    Total XP: <strong><%= newXp %></strong> &nbsp; • &nbsp;
                    Level: <strong><%= newLevel %></strong>
                </p>
            <% } else { %>
                <div class="msg-error">
                    ❌ Incorrect. The correct answer was <strong><%= correct %></strong>
                    (you chose <%= selected %>).
                </div>
                <p style="color:#c0c0e0; margin-top:15px;">
                    Mission: <strong><%= missionTitle %></strong> — no XP awarded.
                </p>
            <% } %>

            <div style="margin-top:25px;">
                <a href="missions" class="btn">Back to Missions</a>
                <a href="dashboard" class="btn btn-secondary">Dashboard</a>
            </div>
        </div>
    </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
    List<Map<String, Object>> players =
        (List<Map<String, Object>>) request.getAttribute("players");
    boolean loggedIn = session.getAttribute("userId") != null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Leaderboard - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 CodeQuest</div>
        <ul>
            <% if (loggedIn) { %>
                <li><a href="dashboard">Dashboard</a></li>
            <% } %>
            <li><a href="missions">Missions</a></li>
            <li><a href="leaderboard">Leaderboard</a></li>
            <% if (loggedIn) { %>
                <li><a href="logout">Logout</a></li>
            <% } else { %>
                <li><a href="login.jsp">Login</a></li>
            <% } %>
        </ul>
    </nav>

    <div class="container">
        <h1 style="color:#00ffc8; margin-bottom:10px;">🏆 Global Leaderboard</h1>
        <p style="color:#8080a0; margin-bottom:30px;">Ranked by total XP earned</p>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
        %>

        <%
            if (players == null || players.isEmpty()) {
        %>
            <div class="card">
                <p style="color:#8080a0;">🎯 No players yet. Be the first to register and start earning XP!</p>
            </div>
        <%
            } else {
        %>
            <table>
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Player</th>
                        <th>Level</th>
                        <th>Missions Completed</th>
                        <th>Total XP</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Map<String, Object> p : players) {
                            int rank = (Integer) p.get("rank");
                            String rankClass = "";
                            String medal = "#" + rank;
                            if (rank == 1) { rankClass = "rank-gold"; medal = "🥇 1"; }
                            else if (rank == 2) { rankClass = "rank-silver"; medal = "🥈 2"; }
                            else if (rank == 3) { rankClass = "rank-bronze"; medal = "🥉 3"; }
                    %>
                    <tr>
                        <td class="<%= rankClass %>"><%= medal %></td>
                        <td><strong><%= p.get("username") %></strong></td>
                        <td><%= p.get("level") %></td>
                        <td><%= p.get("completed") %></td>
                        <td style="color:#00ffc8; font-weight:bold;"><%= p.get("xp") %> XP</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        <%
            }
        %>
    </div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Mission" %>
<%
    Map<String, List<Mission>> missionsByCategory =
        (Map<String, List<Mission>>) request.getAttribute("missionsByCategory");
    Set<Integer> attempted = (Set<Integer>) request.getAttribute("attempted");
    if (attempted == null) attempted = new HashSet<>();
    boolean loggedIn = session.getAttribute("userId") != null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Missions - CodeQuest</title>
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
        <h1 style="color:#00ffc8; margin-bottom:20px;">🎯 Programming Missions</h1>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="msg-error"><%= error %></div>
        <%
            }
        %>

        <%
            if (!loggedIn) {
        %>
            <div class="msg-error">Please <a href="login.jsp" style="color:#ff4757;">login</a> to submit answers.</div>
        <%
            }
        %>

        <%
            if (missionsByCategory == null || missionsByCategory.isEmpty()) {
        %>
            <p style="color:#8080a0;">No missions available yet.</p>
        <%
            } else {
                for (Map.Entry<String, List<Mission>> entry : missionsByCategory.entrySet()) {
                    List<Mission> list = entry.getValue();
                    if (list == null || list.isEmpty()) continue;
        %>
            <h2 style="color:#00ffc8; margin-top:30px;">📚 <%= entry.getKey() %></h2>

            <%
                for (Mission m : list) {
                    String badgeClass = "badge-easy";
                    if ("Medium".equalsIgnoreCase(m.getDifficulty())) badgeClass = "badge-medium";
                    else if ("Hard".equalsIgnoreCase(m.getDifficulty())) badgeClass = "badge-hard";
                    boolean alreadyDone = attempted.contains(m.getId());
            %>
                <div class="mission">
                    <h3><%= m.getTitle() %></h3>
                    <div class="meta">
                        <span class="badge <%= badgeClass %>"><%= m.getDifficulty() %></span>
                        &nbsp; • &nbsp; <strong>+<%= m.getXpReward() %> XP</strong>
                    </div>
                    <p style="margin-bottom:15px; color:#c0c0e0;"><%= m.getDescription() %></p>

                    <% if (alreadyDone) { %>
                        <div class="msg-success">✅ Already attempted — cannot retry.</div>
                    <% } else if (loggedIn) { %>
                        <form action="submit-answer" method="post">
                            <input type="hidden" name="missionId" value="<%= m.getId() %>">
                            <div class="options">
                                <label><input type="radio" name="selectedAnswer" value="A" required> A. <%= m.getOptionA() %></label>
                                <label><input type="radio" name="selectedAnswer" value="B"> B. <%= m.getOptionB() %></label>
                                <label><input type="radio" name="selectedAnswer" value="C"> C. <%= m.getOptionC() %></label>
                                <label><input type="radio" name="selectedAnswer" value="D"> D. <%= m.getOptionD() %></label>
                            </div>
                            <button type="submit" class="btn">Submit Answer</button>
                        </form>
                    <% } else { %>
                        <div class="options">
                            <label>A. <%= m.getOptionA() %></label>
                            <label>B. <%= m.getOptionB() %></label>
                            <label>C. <%= m.getOptionC() %></label>
                            <label>D. <%= m.getOptionD() %></label>
                        </div>
                    <% } %>
                </div>
            <%
                }
            }
            }
        %>
    </div>
</body>
</html>
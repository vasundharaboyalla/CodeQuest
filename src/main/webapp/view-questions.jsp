<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Mission" %>
<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("admin-login.jsp");
        return;
    }
    List<Mission> missions = (List<Mission>) request.getAttribute("missions");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Questions - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 Admin Panel</div>
        <ul>
            <li><a href="admin-dashboard.jsp">Dashboard</a></li>
            <li><a href="add-question.jsp">Add Question</a></li>
            <li><a href="admin-logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
        <div class="card">
            <h2>📋 Manage Questions</h2>

            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <div class="msg-error"><%= error %></div>
            <%
                }
            %>

            <%
                if (missions == null || missions.isEmpty()) {
            %>
                <p style="color:#8080a0;">No questions available yet. Add some missions first.</p>
            <%
                } else {
            %>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Category</th>
                            <th>Difficulty</th>
                            <th>XP</th>
                            <th>Correct</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Mission m : missions) {
                                String badgeClass = "badge-easy";
                                if ("Medium".equalsIgnoreCase(m.getDifficulty())) badgeClass = "badge-medium";
                                else if ("Hard".equalsIgnoreCase(m.getDifficulty())) badgeClass = "badge-hard";
                        %>
                        <tr>
                            <td><%= m.getId() %></td>
                            <td><%= m.getTitle() %></td>
                            <td><%= m.getCategory() %></td>
                            <td><span class="badge <%= badgeClass %>"><%= m.getDifficulty() %></span></td>
                            <td><%= m.getXpReward() %></td>
                            <td><%= m.getCorrectAnswer() %></td>
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
    </div>
</body>
</html>
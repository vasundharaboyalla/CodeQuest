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
    <title>Add Question - CodeQuest</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav>
        <div class="logo">🎮 Admin Panel</div>
        <ul>
            <li><a href="admin-dashboard.jsp">Dashboard</a></li>
            <li><a href="view-questions">View Questions</a></li>
            <li><a href="admin-logout">Logout</a></li>
        </ul>
    </nav>

    <div class="container">
        <div class="card">
            <h2>➕ Add New Mission</h2>

            <%
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                if (error != null) {
            %>
                <div class="msg-error"><%= error %></div>
            <%
                }
                if (success != null) {
            %>
                <div class="msg-success"><%= success %></div>
            <%
                }
            %>

            <form action="add-question" method="post">
                <div class="form-group">
                    <label>Question Title</label>
                    <input type="text" name="title" required maxlength="200">
                </div>

                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" rows="4" required></textarea>
                </div>

                <div class="form-group">
                    <label>Category</label>
                    <select name="category" required>
                        <option value="Java">Java</option>
                        <option value="SQL">SQL</option>
                        <option value="DSA">DSA</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Difficulty</label>
                    <select name="difficulty" required>
                        <option value="Easy">Easy</option>
                        <option value="Medium">Medium</option>
                        <option value="Hard">Hard</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Option A</label>
                    <input type="text" name="optionA" required>
                </div>
                <div class="form-group">
                    <label>Option B</label>
                    <input type="text" name="optionB" required>
                </div>
                <div class="form-group">
                    <label>Option C</label>
                    <input type="text" name="optionC" required>
                </div>
                <div class="form-group">
                    <label>Option D</label>
                    <input type="text" name="optionD" required>
                </div>

                <div class="form-group">
                    <label>Correct Answer</label>
                    <select name="correctAnswer" required>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                        <option value="D">D</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>XP Reward</label>
                    <input type="number" name="xpReward" min="1" max="500" required>
                </div>

                <button type="submit" class="btn">Save Question</button>
                <a href="admin-dashboard.jsp" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>
package servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/add-question")
public class AddQuestionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Auth check
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            resp.sendRedirect("admin-login.jsp");
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String category = req.getParameter("category");
        String difficulty = req.getParameter("difficulty");
        String optionA = req.getParameter("optionA");
        String optionB = req.getParameter("optionB");
        String optionC = req.getParameter("optionC");
        String optionD = req.getParameter("optionD");
        String correctAnswer = req.getParameter("correctAnswer");
        String xpRewardStr = req.getParameter("xpReward");

        // Validation
        if (isBlank(title) || isBlank(description) || isBlank(category)
                || isBlank(difficulty) || isBlank(optionA) || isBlank(optionB)
                || isBlank(optionC) || isBlank(optionD) || isBlank(correctAnswer)
                || isBlank(xpRewardStr)) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("add-question.jsp").forward(req, resp);
            return;
        }

        int xpReward;
        try {
            xpReward = Integer.parseInt(xpRewardStr);
            if (xpReward <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            req.setAttribute("error", "XP reward must be a positive number.");
            req.getRequestDispatcher("add-question.jsp").forward(req, resp);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO missions (title, description, category, difficulty, "
              + "option_a, option_b, option_c, option_d, correct_answer, xp_reward) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, category);
            ps.setString(4, difficulty);
            ps.setString(5, optionA);
            ps.setString(6, optionB);
            ps.setString(7, optionC);
            ps.setString(8, optionD);
            ps.setString(9, correctAnswer);
            ps.setInt(10, xpReward);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                req.setAttribute("success", "Question added successfully!");
            } else {
                req.setAttribute("error", "Failed to add question.");
            }
            req.getRequestDispatcher("add-question.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("add-question.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
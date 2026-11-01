package servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {

    private static final int XP_PER_LEVEL = 100;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        int missionId;
        try {
            missionId = Integer.parseInt(req.getParameter("missionId"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("missions");
            return;
        }
        String selected = req.getParameter("selectedAnswer");

        if (selected == null || selected.trim().isEmpty()) {
            resp.sendRedirect("missions");
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false); // begin transaction

            // Check duplicate
            PreparedStatement dup = con.prepareStatement(
                "SELECT id FROM mission_submissions WHERE user_id=? AND mission_id=?");
            dup.setInt(1, userId);
            dup.setInt(2, missionId);
            ResultSet rsDup = dup.executeQuery();
            if (rsDup.next()) {
                con.rollback();
                req.setAttribute("error", "You already attempted this mission.");
                req.setAttribute("result", "duplicate");
                req.getRequestDispatcher("mission-result.jsp").forward(req, resp);
                return;
            }

            // Fetch correct answer + reward
            PreparedStatement ps = con.prepareStatement(
                "SELECT correct_answer, xp_reward, title FROM missions WHERE id=?");
            ps.setInt(1, missionId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                con.rollback();
                req.setAttribute("error", "Mission not found.");
                req.getRequestDispatcher("mission-result.jsp").forward(req, resp);
                return;
            }

            String correctAnswer = rs.getString("correct_answer");
            int xpReward = rs.getInt("xp_reward");
            String missionTitle = rs.getString("title");

            boolean isCorrect = selected.equalsIgnoreCase(correctAnswer);

            // Insert submission
            PreparedStatement insert = con.prepareStatement(
                "INSERT INTO mission_submissions (user_id, mission_id, selected_answer, is_correct) "
              + "VALUES (?, ?, ?, ?)");
            insert.setInt(1, userId);
            insert.setInt(2, missionId);
            insert.setString(3, selected);
            insert.setBoolean(4, isCorrect);
            insert.executeUpdate();

            int newXp = 0, newLevel = 1;
            if (isCorrect) {
                // Update XP
                PreparedStatement upd = con.prepareStatement(
                    "UPDATE users SET xp = xp + ? WHERE id = ?");
                upd.setInt(1, xpReward);
                upd.setInt(2, userId);
                upd.executeUpdate();

                // Recalculate level
                PreparedStatement getXp = con.prepareStatement(
                    "SELECT xp FROM users WHERE id=?");
                getXp.setInt(1, userId);
                ResultSet rsXp = getXp.executeQuery();
                if (rsXp.next()) {
                    newXp = rsXp.getInt("xp");
                    newLevel = (newXp / XP_PER_LEVEL) + 1;

                    PreparedStatement updLevel = con.prepareStatement(
                        "UPDATE users SET level=? WHERE id=?");
                    updLevel.setInt(1, newLevel);
                    updLevel.setInt(2, userId);
                    updLevel.executeUpdate();

                    session.setAttribute("xp", newXp);
                    session.setAttribute("level", newLevel);
                }
            }

            con.commit();

            req.setAttribute("missionTitle", missionTitle);
            req.setAttribute("isCorrect", isCorrect);
            req.setAttribute("selectedAnswer", selected);
            req.setAttribute("correctAnswer", correctAnswer);
            req.setAttribute("xpReward", xpReward);
            req.setAttribute("newXp", newXp);
            req.setAttribute("newLevel", newLevel);

            req.getRequestDispatcher("mission-result.jsp").forward(req, resp);

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ignore) {}
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("mission-result.jsp").forward(req, resp);
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (SQLException ignore) {}
            try { if (con != null) con.close(); } catch (SQLException ignore) {}
        }
    }
}
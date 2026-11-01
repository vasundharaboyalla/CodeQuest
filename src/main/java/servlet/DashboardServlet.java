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
import model.User;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final int XP_PER_LEVEL = 100; // threshold

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try (Connection con = DBConnection.getConnection()) {

            // Fetch user
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, username, email, xp, level FROM users WHERE id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getInt("xp"),
                    rs.getInt("level")
                );
                req.setAttribute("user", user);

                // Completed missions count
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT COUNT(*) AS total FROM mission_submissions "
                  + "WHERE user_id=? AND is_correct=1");
                ps2.setInt(1, userId);
                ResultSet rs2 = ps2.executeQuery();
                int completed = 0;
                if (rs2.next()) completed = rs2.getInt("total");
                req.setAttribute("completedCount", completed);

                // Progress to next level
                int xpInLevel = user.getId() % XP_PER_LEVEL;
                req.setAttribute("xpInLevel", xpInLevel);
                req.setAttribute("xpToNext", XP_PER_LEVEL - xpInLevel);

                session.setAttribute("xp", user.getId());
                session.setAttribute("level", user.getLevel());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
        }

        req.getRequestDispatcher("dashboard.jsp").forward(req, resp);
    }
}
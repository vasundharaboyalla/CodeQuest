package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Mission;

@WebServlet("/missions")
public class MissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        // Track which missions the user already attempted
        Set<Integer> attempted = new HashSet<>();

        try (Connection con = DBConnection.getConnection()) {

            // Load missions WITHOUT exposing correct_answer
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, title, description, category, difficulty, "
              + "option_a, option_b, option_c, option_d, xp_reward "
              + "FROM missions ORDER BY category, difficulty");
            ResultSet rs = ps.executeQuery();

            // Group by category
            Map<String, List<Mission>> grouped = new LinkedHashMap<>();
            grouped.put("Java", new ArrayList<>());
            grouped.put("SQL", new ArrayList<>());
            grouped.put("DSA", new ArrayList<>());

            while (rs.next()) {
                Mission m = new Mission();
                m.setId(rs.getInt("id"));
                m.setTitle1(rs.getString("title"));
                m.setDescription(rs.getString("description"));
                m.setCategory(rs.getString("category"));
                m.setDifficulty(rs.getString("difficulty"));
                m.setOptionA(rs.getString("option_a"));
                m.setOptionB(rs.getString("option_b"));
                m.setOptionC(rs.getString("option_c"));
                m.setOptionD(rs.getString("option_d"));
                m.setXpReward(rs.getInt("xp_reward"));

                grouped.computeIfAbsent(m.getCategory(), k -> new ArrayList<>()).add(m);
            }

            // Fetch attempted mission IDs for this user
            if (userId != null) {
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT mission_id FROM mission_submissions WHERE user_id=?");
                ps2.setInt(1, userId);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) attempted.add(rs2.getInt("mission_id"));
            }

            req.setAttribute("missionsByCategory", grouped);
            req.setAttribute("attempted", attempted);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
        }

        req.getRequestDispatcher("missions.jsp").forward(req, resp);
    }
}
package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Map<String, Object>> players = new ArrayList<>();

        String sql =
            "SELECT u.id, u.username, u.level, u.xp, " +
            "  (SELECT COUNT(*) FROM mission_submissions ms " +
            "   WHERE ms.user_id = u.id AND ms.is_correct = 1) AS completed " +
            "FROM users u " +
            "ORDER BY u.xp DESC, u.level DESC, u.username ASC";

        try (Connection con = DBConnection.getConnection()) {
            // If we reach here, connection is NOT null
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                int rank = 1;
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("rank", rank++);
                    row.put("id", rs.getInt("id"));
                    row.put("username", rs.getString("username"));
                    row.put("level", rs.getInt("level"));
                    row.put("xp", rs.getInt("xp"));
                    row.put("completed", rs.getInt("completed"));
                    players.add(row);
                }
            }

        } catch (SQLException e) {
            // Because DBConnection now throws SQLException, we catch the REAL error here
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
        }

        req.setAttribute("players", players);
        req.getRequestDispatcher("leaderboard.jsp").forward(req, resp);
    }
}
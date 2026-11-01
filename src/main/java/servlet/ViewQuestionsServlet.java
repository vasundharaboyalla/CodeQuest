package servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import codequest.DBConnection;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Mission;

@WebServlet("/view-questions")
public class ViewQuestionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            resp.sendRedirect("admin-login.jsp");
            return;
        }

        List<Mission> missions = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM missions ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {

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
                m.setCorrectAnswer(rs.getString("correct_answer").charAt(0));
                m.setXpReward(rs.getInt("xp_reward"));
                missions.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
        }

        req.setAttribute("missions", missions);
        RequestDispatcher rd = req.getRequestDispatcher("view-questions.jsp");
        rd.forward(req, resp);
    }
}
package servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String loginId = req.getParameter("loginId");
        String password = req.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, username, xp, level FROM users WHERE (username=? OR email=?) AND password=?");
            ps.setString(1, loginId);
            ps.setString(2, loginId);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", rs.getInt("id"));
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("xp", rs.getInt("xp"));
                session.setAttribute("level", rs.getInt("level"));

                // Add a welcome cookie
                Cookie c = new Cookie("lastUser", rs.getString("username"));
                c.setMaxAge(60 * 60 * 24 * 7);
                resp.addCookie(c);

                resp.sendRedirect("dashboard");
            } else {
                req.setAttribute("error", "Invalid credentials.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        // Validation
        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Check unique username/email
            PreparedStatement check = con.prepareStatement(
                "SELECT id FROM users WHERE username=? OR email=?");
            check.setString(1, username);
            check.setString(2, email);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                req.setAttribute("error", "Username or email already exists.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

            // Insert user
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users (username, email, password, xp, level) VALUES (?, ?, ?, 0, 1)");
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password); // (In production: hash!)
            int rows = ps.executeUpdate();

            if (rows > 0) {
                resp.sendRedirect("login.jsp?msg=registered");
            } else {
                req.setAttribute("error", "Registration failed. Try again.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
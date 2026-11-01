package servlet;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import codequest.DBConnection;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
    urlPatterns = "/admin-login",
    initParams = { @jakarta.servlet.annotation.WebInitParam(name = "adminRole", value = "SUPER_ADMIN") }
)
public class AdminLoginServlet extends HttpServlet {

    private String adminRole; // from ServletConfig

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.adminRole = config.getInitParameter("adminRole");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, username FROM admins WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("adminId", rs.getInt("id"));
                session.setAttribute("adminName", rs.getString("username"));
                session.setAttribute("adminRole", adminRole);

                // ServletContext example: store a shared attribute
                ServletContext ctx = getServletContext();
                Integer loginCount = (Integer) ctx.getAttribute("adminLoginCount");
                if (loginCount == null) loginCount = 0;
                ctx.setAttribute("adminLoginCount", loginCount + 1);

                resp.sendRedirect("admin-dashboard.jsp");
            } else {
                req.setAttribute("error", "Invalid admin credentials.");
                req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
        }
    }
}
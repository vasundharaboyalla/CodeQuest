package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import codequest.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Testing Database Connection...</h1>");

        try (Connection con = DBConnection.getConnection()) {
            if (con != null && !con.isClosed()) {
                out.println("<h2 style='color:green;'>✅ SUCCESS! Connected to MySQL!</h2>");
                out.println("<p>Database Name: " + con.getCatalog() + "</p>");
            }
        } catch (Exception e) {
            out.println("<h2 style='color:red;'>❌ FAILED!</h2>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace(); // Prints to Eclipse console
        }
    }
}
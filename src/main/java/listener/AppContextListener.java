package listener;

import java.sql.Connection;

import codequest.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 CodeQuest application starting...");
        try (Connection con = DBConnection.getConnection()) {
            if (con != null) {
                sce.getServletContext().setAttribute("dbStatus", "OK");
                System.out.println("✅ Database connection successful.");
            }
        } catch (Exception e) {
            sce.getServletContext().setAttribute("dbStatus", "FAILED");
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🛑 CodeQuest application shutting down...");
    }
}
package codequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // ⚠️ 1. We added allowPublicKeyRetrieval=true to fix MySQL 8 Auth issues
    private static final String URL = "jdbc:mysql://localhost:3306/codequest_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "vasundhara@2007"; // ⚠️ CHANGE THIS to your actual MySQL password

    static {
        try {
            // 2. Load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver NOT FOUND! Add mysql-connector-j.jar to WEB-INF/lib");
            e.printStackTrace();
        }
    }

    // 3. This method throws SQLException instead of returning null
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
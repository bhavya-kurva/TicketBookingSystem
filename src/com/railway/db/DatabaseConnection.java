package com.railway.db;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/railway_system";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    /**
     * Opens a connection. Does not print on success (avoids noise when DAOs run).
     * Errors go to stderr with a short message.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found.");
            System.err.println("Add mysql-connector-j-*.jar to the lib folder and include it on the classpath (see run-TestDB.ps1).");
            return null;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MySQL connection test ===\n");

        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Result: FAILED (no connection).");
                System.out.println("Check: MySQL running, database railway_system exists, user/password in DatabaseConnection.java.");
                return;
            }

            System.out.println("Result: connected to railway_system.");
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("\n--- Server metadata ---");
            System.out.println("Product:  " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("Driver:   " + meta.getDriverName());
            System.out.println("JDBC URL: " + meta.getURL());
            System.out.println("-----------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

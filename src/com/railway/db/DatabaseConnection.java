package com.railway.db;

import java.sql.*;

public class DatabaseConnection {
    
    
    private static final String URL = "jdbc:mysql://localhost:3306/railway_system";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";  
    
    public static Connection getConnection() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL Database!");
            System.out.println("Database: railway_system");
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println(" MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-java.jar is in lib folder");
            return null;
            
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Testing MySQL Connection...\n");
        
        try (Connection conn = getConnection()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("\n=== DATABASE INFO ===");
                System.out.println("Database: " + meta.getDatabaseProductName());
                System.out.println("Version: " + meta.getDatabaseProductVersion());
                System.out.println("Driver: " + meta.getDriverName());
                System.out.println("URL: " + meta.getURL());
                System.out.println("=====================");
                System.out.println("\nConnection successful!");
            }
        } catch (SQLException e) {
            System.err.println("Database test failed!");
            System.err.println("Error: " + e.getMessage());
        }
    }
}
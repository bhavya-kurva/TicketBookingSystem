package com.railway.db;

import java.sql.*;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/railway_system";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";  
    
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Database Connected!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found!");
            return null;
        } catch (SQLException e) {
            System.err.println(" Connection failed: " + e.getMessage());
            return null;
        }
    }
}
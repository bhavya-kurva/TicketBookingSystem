import com.railway.db.DatabaseConnection;
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("=== RAILWAY SYSTEM - DATABASE TEST ===\n");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Cannot connect to database!");
                return;
            }
            
            
            System.out.println("1. Fetching Users:");
            System.out.println("-------------------");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("   ID: " + rs.getInt("user_id") + 
                                 " | Username: " + rs.getString("username") +
                                 " | Name: " + rs.getString("full_name"));
            }
            if (!hasData) {
                System.out.println("   No users found");
            }
            
            
            System.out.println("\n2. Fetching Trains:");
            System.out.println("-------------------");
            rs = stmt.executeQuery("SELECT * FROM trains");
            
            hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("   " + rs.getString("train_number") + 
                                 " | " + rs.getString("train_name") +
                                 " | " + rs.getString("from_station") + " → " + 
                                 rs.getString("to_station") +
                                 " | ₹" + rs.getDouble("fare"));
            }
            if (!hasData) {
                System.out.println("   No trains found");
            }
            
            rs.close();
            stmt.close();
            
            System.out.println("\nDatabase test completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

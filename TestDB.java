import com.railway.dao.TrainDAO;
import com.railway.dao.UserDAO;
import com.railway.db.DatabaseConnection;
import com.railway.model.Train;
import com.railway.model.User;
import java.sql.*;
import java.util.List;

/**
 * Console demo: raw SQL + DAO usage. Run with MySQL and Connector/J on classpath
 * (see run-TestDB.ps1).
 */
public class TestDB {

    private static void printSection(String title) {
        System.out.println();
        System.out.println(title);
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("=== RAILWAY TICKET BOOKING SYSTEM - DATABASE / DAO TEST ===");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("\nCannot open a database connection.");
                System.out.println("Use: .\\run-TestDB.ps1   or   java -cp \"out;lib\\mysql-connector-j-8.4.0.jar\" TestDB");
                return;
            }

            printSection("1. Users (SQL)");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT user_id, username, full_name, role FROM users ORDER BY user_id")) {
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.printf("   %d | %-12s | %-20s | %s%n",
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("full_name"),
                            rs.getString("role"));
                }
                if (n == 0) {
                    System.out.println("   (no rows)");
                }
            }

            printSection("2. Trains (SQL)");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT train_number, train_name, from_station, to_station, seats_available, fare FROM trains ORDER BY train_id")) {
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.printf("   %s | %s | %s -> %s | seats: %d | Rs. %.2f%n",
                            rs.getString("train_number"),
                            rs.getString("train_name"),
                            rs.getString("from_station"),
                            rs.getString("to_station"),
                            rs.getInt("seats_available"),
                            rs.getDouble("fare"));
                }
                if (n == 0) {
                    System.out.println("   (no rows)");
                }
            }

            printSection("3. TrainDAO.searchTrains(\"Delhi\", \"Mumbai\")");
            TrainDAO trainDAO = new TrainDAO();
            List<Train> found = trainDAO.searchTrains("Delhi", "Mumbai");
            if (found.isEmpty()) {
                System.out.println("   (no trains for this route)");
            } else {
                for (Train t : found) {
                    System.out.printf("   %s %s | Rs. %.2f | seats: %d%n",
                            t.getTrainNumber(), t.getTrainName(), t.getFare(), t.getSeatsAvailable());
                }
            }

            printSection("4. UserDAO.login(\"raj\", \"raj123\")");
            UserDAO userDAO = new UserDAO();
            User u = userDAO.login("raj", "raj123");
            if (u != null) {
                System.out.println("   OK - logged in as: " + u.getFullName() + " (" + u.getRole() + ")");
            } else {
                System.out.println("   FAILED — invalid credentials or user missing");
            }

            System.out.println();
            System.out.println("=== All checks finished successfully ===");
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

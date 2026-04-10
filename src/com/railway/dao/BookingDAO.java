package com.railway.dao;
import com.railway.db.DatabaseConnection;
import com.railway.model.Booking;
import com.railway.model.Train;
import java.sql.*;
import java.util.*;


public class BookingDAO {
        private TrainDAO trainDAO = new TrainDAO();
    
    // Generate unique PNR
    private String generatePNR() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", new Random().nextInt(10000));
        return timestamp.substring(timestamp.length() - 6) + random;
    }
    
    // Create new booking
    public boolean createBooking(Booking booking) {
        String pnr = generatePNR();
        String sql = "INSERT INTO bookings (booking_id, pnr, user_id, train_id, " +
                     "from_station, to_station, journey_date, passengers, total_fare, status) " +
                     "VALUES (bookings_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pnr);
            pstmt.setInt(2, booking.getUserId());
            pstmt.setInt(3, booking.getTrainId());
            pstmt.setString(4, booking.getFromStation());
            pstmt.setString(5, booking.getToStation());
            pstmt.setDate(6, booking.getJourneyDate());
            pstmt.setInt(7, booking.getPassengers());
            pstmt.setDouble(8, booking.getTotalFare());
            pstmt.setString(9, "confirmed");
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                // Update seat availability
                trainDAO.updateSeatAvailability(booking.getTrainId(), booking.getPassengers());
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get booking by PNR
    public Booking getBookingByPNR(String pnr) {
        String sql = "SELECT * FROM bookings WHERE pnr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pnr);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setPnr(rs.getString("pnr"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTrainId(rs.getInt("train_id"));
                booking.setFromStation(rs.getString("from_station"));
                booking.setToStation(rs.getString("to_station"));
                booking.setJourneyDate(rs.getDate("journey_date"));
                booking.setPassengers(rs.getInt("passengers"));
                booking.setTotalFare(rs.getDouble("total_fare"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setStatus(rs.getString("status"));
                
                // Get train details
                Train train = trainDAO.getTrainById(booking.getTrainId());
                booking.setTrain(train);
                
                return booking;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

}
}

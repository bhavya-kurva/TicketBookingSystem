package com.railway.dao;
import com.railway.db.DatabaseConnection;
import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.model.Train;
import java.sql.*;
import java.util.*;


public class BookingDAO {
    private TrainDAO trainDAO = new TrainDAO();

    
    private String generatePNR() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", new Random().nextInt(10000));
        return timestamp.substring(timestamp.length() - 6) + random;
    }
    
    
    public boolean createBooking(Booking booking) {
        return createBooking(booking, Collections.emptyList());
    }

    public boolean createBooking(Booking booking, List<Passenger> passengers) {
        String pnr = generatePNR();
        String sql = "INSERT INTO bookings (pnr, user_id, train_id, journey_date, " +
                     "passengers, total_fare, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return false;
        }
        try {
            conn.setAutoCommit(false);
            booking.setPnr(pnr);
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, pnr);
                pstmt.setInt(2, booking.getUserId());
                pstmt.setInt(3, booking.getTrainId());
                pstmt.setDate(4, booking.getJourneyDate());
                pstmt.setInt(5, booking.getPassengers());
                pstmt.setDouble(6, booking.getTotalFare());
                pstmt.setString(7, "confirmed");
                if (pstmt.executeUpdate() <= 0) {
                    conn.rollback();
                    return false;
                }
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setBookingId(generatedKeys.getInt(1));
                    }
                }
            }
            if (!trainDAO.updateSeatAvailability(conn, booking.getTrainId(), booking.getPassengers())) {
                conn.rollback();
                return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public Booking getBookingByPNR(String pnr) {
        String sql = "SELECT * FROM bookings WHERE pnr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return null;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, pnr);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setPnr(rs.getString("pnr"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setTrainId(rs.getInt("train_id"));
                    booking.setJourneyDate(rs.getDate("journey_date"));
                    booking.setPassengers(rs.getInt("passengers"));
                    booking.setTotalFare(rs.getDouble("total_fare"));
                    booking.setBookingDate(rs.getTimestamp("booking_date"));
                    booking.setStatus(rs.getString("status"));
                    
                    Train train = trainDAO.getTrainById(booking.getTrainId());
                    booking.setTrain(train);
                    if (train != null) {
                        booking.setFromStation(train.getFromStation());
                        booking.setToStation(train.getToStation());
                    }
                    
                    return booking;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

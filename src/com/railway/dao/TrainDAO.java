package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Train;
import java.sql.*;
import java.util.*;

public class TrainDAO {
    
    public List<Train> searchTrains(String fromStation, String toStation) {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains WHERE from_station = ? AND to_station = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return trains;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, fromStation);
                pstmt.setString(2, toStation);
                
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Train train = new Train();
                    train.setTrainId(rs.getInt("train_id"));
                    train.setTrainNumber(rs.getString("train_number"));
                    train.setTrainName(rs.getString("train_name"));
                    train.setFromStation(rs.getString("from_station"));
                    train.setToStation(rs.getString("to_station"));
                    train.setDepartureTime(rs.getString("departure_time"));
                    train.setArrivalTime(rs.getString("arrival_time"));
                    train.setSeatsAvailable(rs.getInt("seats_available"));
                    train.setFare(rs.getDouble("fare"));
                    trains.add(train);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }

    public Train getTrainById(int trainId) {
        String sql = "SELECT * FROM trains WHERE train_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return null;
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    Train train = new Train();
                    train.setTrainId(rs.getInt("train_id"));
                    train.setTrainNumber(rs.getString("train_number"));
                    train.setTrainName(rs.getString("train_name"));
                    train.setFromStation(rs.getString("from_station"));
                    train.setToStation(rs.getString("to_station"));
                    train.setDepartureTime(rs.getString("departure_time"));
                    train.setArrivalTime(rs.getString("arrival_time"));
                    train.setSeatsAvailable(rs.getInt("seats_available"));
                    train.setFare(rs.getDouble("fare"));
                    return train;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSeatAvailability(Connection conn, int trainId, int seatsBooked) throws SQLException {
        String sql = "UPDATE trains SET seats_available = seats_available - ? "
                     + "WHERE train_id = ? AND seats_available >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatsBooked);
            pstmt.setInt(2, trainId);
            pstmt.setInt(3, seatsBooked);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateSeatAvailability(int trainId, int seatsBooked) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return false;
            }
            return updateSeatAvailability(conn, trainId, seatsBooked);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

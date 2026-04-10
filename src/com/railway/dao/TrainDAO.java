package com.railway.dao;

import com.railway.db.DatabaseConnection;
import com.railway.model.Train;
import java.sql.*;
import java.util.*;

public class TrainDAO {
    
    public List<Train> searchTrains(String fromStation, String toStation) {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT * FROM trains WHERE from_station = ? AND to_station = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }
}

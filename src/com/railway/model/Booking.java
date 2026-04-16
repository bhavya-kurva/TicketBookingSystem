package com.railway.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Booking {
    private int bookingId;
    private String pnr;
    private int userId;
    private int trainId;
    private String fromStation;
    private String toStation;
    private Date journeyDate;
    private Timestamp bookingDate;
    private double totalFare;
    private String status;
    private int passengers;
    private String classType;
    private List<Passenger> passengerList;
    private Train train;
    
    public Booking() {}
    
    public Booking(String pnr, int userId, int trainId, String fromStation, 
                   String toStation, Date journeyDate, double totalFare, int passengers) {
        this.pnr = pnr;
        this.userId = userId;
        this.trainId = trainId;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.journeyDate = journeyDate;
        this.totalFare = totalFare;
        this.passengers = passengers;
        this.status = "CONFIRMED";
    }
    
   
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getTrainId() { return trainId; }
    public void setTrainId(int trainId) { this.trainId = trainId; }
    
    public String getFromStation() { return fromStation; }
    public void setFromStation(String fromStation) { this.fromStation = fromStation; }
    
    public String getToStation() { return toStation; }
    public void setToStation(String toStation) { this.toStation = toStation; }
    
    public Date getJourneyDate() { return journeyDate; }
    public void setJourneyDate(Date journeyDate) { this.journeyDate = journeyDate; }
    
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }
    
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    
    public List<Passenger> getPassengerList() { return passengerList; }
    public void setPassengerList(List<Passenger> passengerList) { this.passengerList = passengerList; }
    
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
}
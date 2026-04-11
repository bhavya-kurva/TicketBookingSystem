package com.railway.model;

import java.sql.Date;

public class Booking {
    private int bookingId;
    private String pnr;
    private int userId;
    private int trainId;
    private String fromStation;
    private String toStation;
    private Date journeyDate;
    private int passengers;
    private double totalFare;
    private Date bookingDate;
    private String status;
    private Train train;
    
    public Booking() {}
    
    // Getters and Setters
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
    
    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }
    
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    
    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
}

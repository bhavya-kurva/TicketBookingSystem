package com.railway.model;

public class Train {
    private int trainId;
    private String trainNumber;
    private String trainName;
    private String fromStation;
    private String toStation;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private int seatsAvailable;
    private double fare;
    
    public Train() {}
    
    public int getTrainId() { return trainId; }
    public String getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getFromStation() { return fromStation; }
    public String getToStation() { return toStation; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDuration() { return duration; }
    public int getSeatsAvailable() { return seatsAvailable; }
    public double getFare() { return fare; }
    
    
    public void setTrainId(int trainId) { this.trainId = trainId; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public void setFromStation(String fromStation) { this.fromStation = fromStation; }
    public void setToStation(String toStation) { this.toStation = toStation; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }
    public void setFare(double fare) { this.fare = fare; }
}

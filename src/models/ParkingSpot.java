package models;

import enums.SpotType;
import observers.SpotObserver;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpot {
    private final int spotId;
    private final SpotType type;
    private final boolean hasChargingPoint;
    private boolean isAvailable;
    private Vehicle currentVehicle;
    private final List<SpotObserver> observers = new ArrayList<>();

    public ParkingSpot(int spotId, SpotType type, boolean hasChargingPoint) {
        this.spotId = spotId;
        this.type = type;
        this.hasChargingPoint = hasChargingPoint;
        this.isAvailable = true;
    }

    public synchronized boolean park(Vehicle vehicle) {
        if (!isAvailable) {
            return false;
        }
        this.currentVehicle = vehicle;
        this.isAvailable = false;
        notifyObservers();
        return true;
    }

    public synchronized void vacate() {
        this.currentVehicle = null;
        this.isAvailable = true;
        notifyObservers();
    }

    // Observer pattern
    public void addObserver(SpotObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (SpotObserver observer : observers) {
            observer.onSpotStatusChanged(this);
        }
    }

    // Getters
    public int getSpotId() {
        return spotId;
    }

    public SpotType getType() {
        return type;
    }

    public boolean hasChargingPoint() {
        return hasChargingPoint;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    @Override
    public String toString() {
        return String.format("Spot %d (%s, %s)",
                spotId, type, isAvailable ? "Available" : "Occupied");
    }
}
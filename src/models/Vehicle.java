package models;

import enums.VehicleSize;

public class Vehicle {
    private String vehicleNumber;
    private VehicleSize size;
    private boolean isElectric;
    private boolean needsCharging;

    public Vehicle(String vehicleNumber, VehicleSize size) {
        this.vehicleNumber = vehicleNumber;
        this.size = size;
        this.isElectric = false;
        this.needsCharging = false;
    }

    public Vehicle(String vehicleNumber, VehicleSize size, boolean isElectric, boolean needsCharging) {
        this.vehicleNumber = vehicleNumber;
        this.size = size;
        this.isElectric = isElectric;
        this.needsCharging = needsCharging;
    }

    // Getters and Setters
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleSize getSize() {
        return size;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public boolean needsCharging() {
        return needsCharging;
    }

    public void setElectric(boolean electric) {
        this.isElectric = electric;
    }

    public void setNeedsCharging(boolean needsCharging) {
        this.needsCharging = needsCharging;
    }
}
package models;

import java.time.LocalDateTime;

public class Ticket {
    private static int COUNTER = 1;
    private final int ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private final boolean usingCharging;

    public Ticket(Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = COUNTER++;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        // Vehicle is using charging if it's electric, needs charging, and spot has
        // charging
        this.usingCharging = vehicle.isElectric() &&
                vehicle.needsCharging() &&
                spot.hasChargingPoint();
    }

    // Getters
    public int getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public boolean isUsingCharging() {
        return usingCharging;
    }
}
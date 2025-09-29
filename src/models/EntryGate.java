package models;

public class EntryGate {
    private final int gateId;
    private final ParkingLot parkingLot;

    public EntryGate(int gateId, ParkingLot parkingLot) {
        this.gateId = gateId;
        this.parkingLot = parkingLot;
    }

    public Ticket generateTicket(Vehicle vehicle) {
        ParkingSpot spot = parkingLot.findAvailableSpot(vehicle, gateId);
        if (spot == null) {
            return null; // No available spot
        }

        if (spot.park(vehicle)) {
            return new Ticket(vehicle, spot);
        }

        return null; // Spot was taken just before parking
    }

    public int getGateId() {
        return gateId;
    }
}
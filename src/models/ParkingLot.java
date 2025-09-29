package models;

import strategies.SlotAllocationStrategy;
import java.util.List;
import java.util.ArrayList;

public class ParkingLot {
    private final int lotId;
    private final List<ParkingSpot> spots;
    private final SlotAllocationStrategy allocationStrategy;
    private final List<EntryGate> entryGates;
    private final List<ExitGate> exitGates;
    private final int totalFloors;

    public ParkingLot(int lotId, List<ParkingSpot> spots,
            SlotAllocationStrategy allocationStrategy, int totalFloors) {
        this.lotId = lotId;
        this.spots = spots;
        this.allocationStrategy = allocationStrategy;
        this.totalFloors = totalFloors;
        this.entryGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
    }

    public synchronized ParkingSpot findAvailableSpot(Vehicle vehicle, int entryGateId) {
        return allocationStrategy.findSpot(spots, vehicle, entryGateId);
    }

    public void addEntryGate(EntryGate gate) {
        entryGates.add(gate);
    }

    public void addExitGate(ExitGate gate) {
        exitGates.add(gate);
    }

    // Getters
    public int getLotId() {
        return lotId;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public List<EntryGate> getEntryGates() {
        return entryGates;
    }

    public List<ExitGate> getExitGates() {
        return exitGates;
    }

    public int getTotalFloors() {
        return totalFloors;
    }
}
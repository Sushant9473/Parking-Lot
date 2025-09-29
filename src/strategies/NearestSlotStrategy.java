package strategies;

import models.ParkingSpot;
import models.Vehicle;
import enums.SpotType;
import enums.VehicleSize;
import java.util.List;

public class NearestSlotStrategy implements SlotAllocationStrategy {

    @Override
    public ParkingSpot findSpot(List<ParkingSpot> spots, Vehicle vehicle, int entryGateId) {
        // Find the first available spot that can accommodate the vehicle
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && canAccommodate(spot, vehicle)) {
                return spot;
            }
        }
        return null; // No available spot found
    }

    private boolean canAccommodate(ParkingSpot spot, Vehicle vehicle) {
        SpotType spotType = spot.getType();
        VehicleSize vehicleSize = vehicle.getSize();

        // Electric vehicles can use electric spots or regular spots based on their size
        if (vehicle.isElectric() && vehicle.needsCharging()) {
            // Must use electric spot with charging
            return spotType == SpotType.ELECTRIC;
        }

        // Regular compatibility check
        switch (vehicleSize) {
            case SMALL:
                return spotType == SpotType.SMALL || spotType == SpotType.MEDIUM ||
                        spotType == SpotType.LARGE || spotType == SpotType.ELECTRIC;
            case MEDIUM:
                return spotType == SpotType.MEDIUM || spotType == SpotType.LARGE ||
                        spotType == SpotType.ELECTRIC;
            case LARGE:
                return spotType == SpotType.LARGE;
            default:
                return false;
        }
    }
}
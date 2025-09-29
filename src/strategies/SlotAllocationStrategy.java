package strategies;

import models.ParkingSpot;
import models.Vehicle;
import java.util.List;

public interface SlotAllocationStrategy {
    ParkingSpot findSpot(List<ParkingSpot> spots, Vehicle vehicle, int entryGateId);
}
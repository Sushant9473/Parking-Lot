package observers;

import models.ParkingSpot;

public interface SpotObserver {
    void onSpotStatusChanged(ParkingSpot spot);
}

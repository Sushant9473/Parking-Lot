package observers;

import models.ParkingSpot;

public class DisplayBoard implements SpotObserver {

    @Override
    public void onSpotStatusChanged(ParkingSpot spot) {
        System.out.println("📢 Display Board: " + spot);
    }
}

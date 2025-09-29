package factory;

import models.*;
import strategies.*;
import enums.SpotType;
import java.util.*;

public class ParkingLotFactory {

    public static class ParkingLotBuilder {
        private int lotId = 1;
        private int floors = 1;
        private int spotsPerFloor = 10;
        private int entryGates = 1;
        private int exitGates = 1;
        private SlotAllocationStrategy allocationStrategy = new NearestSlotStrategy();
        private PricingStrategy pricingStrategy;

        public ParkingLotBuilder withLotId(int lotId) {
            this.lotId = lotId;
            return this;
        }

        public ParkingLotBuilder withFloors(int floors) {
            this.floors = floors;
            return this;
        }

        public ParkingLotBuilder withSpotsPerFloor(int spotsPerFloor) {
            this.spotsPerFloor = spotsPerFloor;
            return this;
        }

        public ParkingLotBuilder withEntryGates(int entryGates) {
            this.entryGates = entryGates;
            return this;
        }

        public ParkingLotBuilder withExitGates(int exitGates) {
            this.exitGates = exitGates;
            return this;
        }

        public ParkingLotBuilder withAllocationStrategy(SlotAllocationStrategy strategy) {
            this.allocationStrategy = strategy;
            return this;
        }

        public ParkingLotBuilder withPricingStrategy(PricingStrategy strategy) {
            this.pricingStrategy = strategy;
            return this;
        }

        public ParkingLot build() {
            List<ParkingSpot> allSpots = new ArrayList<>();

            // Calculate spot distribution per floor
            int smallSpots = (int) (spotsPerFloor * 0.4); // 40% small
            int mediumSpots = (int) (spotsPerFloor * 0.3); // 30% medium
            int largeSpots = (int) (spotsPerFloor * 0.2); // 20% large
            int electricSpots = spotsPerFloor - smallSpots - mediumSpots - largeSpots; // remaining as electric

            // Create spots for each floor
            for (int floor = 1; floor <= floors; floor++) {
                int spotPosition = 1;

                // Add small spots
                for (int i = 0; i < smallSpots; i++) {
                    int spotId = floor * 100 + spotPosition++;
                    allSpots.add(new ParkingSpot(spotId, SpotType.SMALL, false));
                }

                // Add medium spots
                for (int i = 0; i < mediumSpots; i++) {
                    int spotId = floor * 100 + spotPosition++;
                    allSpots.add(new ParkingSpot(spotId, SpotType.MEDIUM, false));
                }

                // Add large spots
                for (int i = 0; i < largeSpots; i++) {
                    int spotId = floor * 100 + spotPosition++;
                    allSpots.add(new ParkingSpot(spotId, SpotType.LARGE, false));
                }

                // Add electric spots (with charging points)
                for (int i = 0; i < electricSpots; i++) {
                    int spotId = floor * 100 + spotPosition++;
                    allSpots.add(new ParkingSpot(spotId, SpotType.ELECTRIC, true));
                }
            }

            // Create parking lot
            ParkingLot lot = new ParkingLot(lotId, allSpots, allocationStrategy, floors);

            // Add entry gates
            for (int i = 1; i <= entryGates; i++) {
                lot.addEntryGate(new EntryGate(i, lot));
            }

            // Add exit gates
            for (int i = 1; i <= exitGates; i++) {
                lot.addExitGate(new ExitGate(i, pricingStrategy));
            }

            return lot;
        }
    }

    public static ParkingLot createDefaultParkingLot() {
        // Default pricing rates
        Map<SpotType, Double> rates = new HashMap<>();
        rates.put(SpotType.SMALL, 10.0);
        rates.put(SpotType.MEDIUM, 20.0);
        rates.put(SpotType.LARGE, 30.0);
        rates.put(SpotType.ELECTRIC, 25.0);

        PricingStrategy pricingStrategy = new PerHourPricingStrategy(rates, 5.0);

        return new ParkingLotBuilder()
                .withFloors(3)
                .withSpotsPerFloor(20)
                .withEntryGates(2)
                .withExitGates(2)
                .withPricingStrategy(pricingStrategy)
                .build();
    }
}
import factory.ParkingLotFactory;
import models.*;
import enums.VehicleSize;
import java.util.*;

public class Main {
    private static Map<String, Ticket> ticketStorage = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🚗 Welcome to Parking Lot Management System 🚗\n");

        // Create parking lot using factory
        ParkingLot parkingLot = ParkingLotFactory.createDefaultParkingLot();

        displayParkingLotInfo(parkingLot);

        // Start interactive system
        runParkingSystem(parkingLot);
    }

    private static void displayParkingLotInfo(ParkingLot lot) {
        System.out.println("Parking Lot Created Successfully!");
        System.out.println("- Total Floors: " + lot.getTotalFloors());
        System.out.println("- Total Spots: " + lot.getSpots().size());
        System.out.println("- Entry Gates: " + lot.getEntryGates().size());
        System.out.println("- Exit Gates: " + lot.getExitGates().size());
        System.out.println();
    }

    private static void runParkingSystem(ParkingLot lot) {
        while (true) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    parkVehicle(lot);
                    break;
                case 2:
                    exitVehicle(lot);
                    break;
                case 3:
                    displayParkingStatus(lot);
                    break;
                case 4:
                    System.out.println("Thank you for using our Parking Lot System! 👋");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.\n");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("=== Parking Lot Management System ===");
        System.out.println("1. Park Vehicle 🚗");
        System.out.println("2. Exit Vehicle 🚪");
        System.out.println("3. View Parking Status 📊");
        System.out.println("4. Exit System 🚪");
        System.out.print("Choose an option (1-4): ");
    }

    private static int getChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // consume invalid input
            return -1;
        }
    }

    private static void parkVehicle(ParkingLot lot) {
        System.out.println("\n🚗 === Park Vehicle ===");

        // Get vehicle details
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.nextLine().trim().toUpperCase();

        if (vehicleNumber.isEmpty()) {
            System.out.println("❌ Vehicle number cannot be empty.\n");
            return;
        }

        if (ticketStorage.containsKey(vehicleNumber)) {
            System.out.println("❌ Vehicle " + vehicleNumber + " is already parked!\n");
            return;
        }

        // Get vehicle size
        VehicleSize size = getVehicleSize();
        if (size == null)
            return;

        // Get electric vehicle preferences
        boolean isElectric = false;
        boolean needsCharging = false;

        System.out.print("Is this an electric vehicle? (y/n): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            isElectric = true;
            System.out.print("Do you need charging? (y/n): ");
            needsCharging = scanner.nextLine().trim().toLowerCase().startsWith("y");
        }

        // Select entry gate
        int gateChoice = selectEntryGate(lot);
        if (gateChoice == -1)
            return;

        // Create vehicle and generate ticket
        Vehicle vehicle = new Vehicle(vehicleNumber, size, isElectric, needsCharging);
        EntryGate entryGate = lot.getEntryGates().get(gateChoice - 1);
        Ticket ticket = entryGate.generateTicket(vehicle);

        if (ticket != null) {
            ticketStorage.put(vehicleNumber, ticket);
            displayParkingSuccess(ticket, gateChoice);
        } else {
            System.out.println("❌ Sorry! No suitable parking spot available.\n");
        }
    }

    private static VehicleSize getVehicleSize() {
        System.out.println("Select vehicle size:");
        System.out.println("1. Small (Bike/Motorcycle)");
        System.out.println("2. Medium (Car/SUV)");
        System.out.println("3. Large (Bus/Truck)");
        System.out.print("Choice (1-3): ");

        int choice = getChoice();
        switch (choice) {
            case 1:
                return VehicleSize.SMALL;
            case 2:
                return VehicleSize.MEDIUM;
            case 3:
                return VehicleSize.LARGE;
            default:
                System.out.println("❌ Invalid choice.\n");
                return null;
        }
    }

    private static int selectEntryGate(ParkingLot lot) {
        System.out.println("Select entry gate:");
        for (int i = 0; i < lot.getEntryGates().size(); i++) {
            System.out.println((i + 1) + ". Entry Gate " + (i + 1));
        }
        System.out.print("Choice: ");

        int choice = getChoice();
        if (choice >= 1 && choice <= lot.getEntryGates().size()) {
            return choice;
        } else {
            System.out.println("❌ Invalid gate selection.\n");
            return -1;
        }
    }

    private static void displayParkingSuccess(Ticket ticket, int gateChoice) {
        System.out.println("\n✅ Vehicle parked successfully!");
        System.out.println("📋 Ticket Details:");
        System.out.println("   - Ticket ID: " + ticket.getTicketId());
        System.out.println("   - Vehicle: " + ticket.getVehicle().getVehicleNumber());
        System.out.println("   - Spot: " + ticket.getSpot().getSpotId());
        System.out.println("   - Spot Type: " + ticket.getSpot().getType());
        System.out.println("   - Entry Gate: " + gateChoice);
        System.out.println("   - Entry Time: " + ticket.getEntryTime());
        if (ticket.isUsingCharging()) {
            System.out.println("   - Charging: Active 🔌");
        }
        System.out.println();
    }

    private static void exitVehicle(ParkingLot lot) {
        System.out.println("\n🚪 === Exit Vehicle ===");

        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.nextLine().trim().toUpperCase();

        Ticket ticket = ticketStorage.get(vehicleNumber);
        if (ticket == null) {
            System.out.println("❌ No parking ticket found for vehicle: " + vehicleNumber + "\n");
            return;
        }

        // Select exit gate
        int gateChoice = selectExitGate(lot);
        if (gateChoice == -1)
            return;

        // Process exit
        ExitGate exitGate = lot.getExitGates().get(gateChoice - 1);
        double amount = exitGate.processExit(ticket);

        // Remove from storage
        ticketStorage.remove(vehicleNumber);

        displayExitSuccess(ticket, amount, gateChoice);
    }

    private static int selectExitGate(ParkingLot lot) {
        System.out.println("Select exit gate:");
        for (int i = 0; i < lot.getExitGates().size(); i++) {
            System.out.println((i + 1) + ". Exit Gate " + (i + 1));
        }
        System.out.print("Choice: ");

        int choice = getChoice();
        if (choice >= 1 && choice <= lot.getExitGates().size()) {
            return choice;
        } else {
            System.out.println("❌ Invalid gate selection.\n");
            return -1;
        }
    }

    private static void displayExitSuccess(Ticket ticket, double amount, int gateChoice) {
        System.out.println("\n✅ Vehicle exited successfully!");
        System.out.println("💰 Payment Details:");
        System.out.println("   - Vehicle: " + ticket.getVehicle().getVehicleNumber());
        System.out.println("   - Spot: " + ticket.getSpot().getSpotId());
        System.out.println("   - Entry Time: " + ticket.getEntryTime());
        System.out.println("   - Exit Gate: " + gateChoice);
        System.out.println("   - Total Amount: $" + String.format("%.2f", amount));
        System.out.println("   - Thank you for using our service! 🙏\n");
    }

    private static void displayParkingStatus(ParkingLot lot) {
        System.out.println("\n📊 === Parking Status ===");

        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("Available", 0);
        statusCount.put("Occupied", 0);

        for (ParkingSpot spot : lot.getSpots()) {
            if (spot.isAvailable()) {
                statusCount.put("Available", statusCount.get("Available") + 1);
            } else {
                statusCount.put("Occupied", statusCount.get("Occupied") + 1);
            }
        }

        System.out.println("Total Spots: " + lot.getSpots().size());
        System.out.println("Available: " + statusCount.get("Available"));
        System.out.println("Occupied: " + statusCount.get("Occupied"));
        System.out.println("Currently Parked Vehicles: " + ticketStorage.size());
        System.out.println();
    }
}
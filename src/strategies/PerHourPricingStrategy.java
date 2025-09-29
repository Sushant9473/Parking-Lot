package strategies;

import models.Ticket;
import enums.SpotType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class PerHourPricingStrategy implements PricingStrategy {
    private final Map<SpotType, Double> hourlyRates;
    private final double chargingFeePerHour;

    public PerHourPricingStrategy(Map<SpotType, Double> hourlyRates, double chargingFeePerHour) {
        this.hourlyRates = hourlyRates;
        this.chargingFeePerHour = chargingFeePerHour;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        LocalDateTime entryTime = ticket.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();

        // Calculate hours (minimum 1 hour)
        long minutes = ChronoUnit.MINUTES.between(entryTime, exitTime);
        long hours = Math.max(1, (minutes + 59) / 60); // Round up to nearest hour

        SpotType spotType = ticket.getSpot().getType();
        double baseRate = hourlyRates.getOrDefault(spotType, 10.0); // Default rate
        double totalAmount = baseRate * hours;

        // Add charging fee if applicable
        if (ticket.isUsingCharging()) {
            totalAmount += chargingFeePerHour * hours;
        }

        return totalAmount;
    }
}
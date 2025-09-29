package models;

import strategies.PricingStrategy;

public class ExitGate {
    private final int gateId;
    private final PricingStrategy pricingStrategy;

    public ExitGate(int gateId, PricingStrategy pricingStrategy) {
        this.gateId = gateId;
        this.pricingStrategy = pricingStrategy;
    }

    public double processExit(Ticket ticket) {
        double amount = pricingStrategy.calculatePrice(ticket);
        ticket.getSpot().vacate();
        return amount;
    }

    public int getGateId() {
        return gateId;
    }
}
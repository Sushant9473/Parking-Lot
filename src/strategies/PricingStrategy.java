package strategies;

import models.Ticket;

public interface PricingStrategy {
    double calculatePrice(Ticket ticket);
}
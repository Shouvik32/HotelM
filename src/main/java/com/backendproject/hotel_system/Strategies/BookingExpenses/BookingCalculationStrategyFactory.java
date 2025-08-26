package com.backendproject.hotel_system.Strategies.BookingExpenses;


import org.springframework.stereotype.Component;

@Component
public class BookingCalculationStrategyFactory {
    public static ExpenseCalculationStrategy getStrategy(StrategyType type) {
        System.out.println(type+"create");
        switch (type) {
            case SURCHARGE:
                return new SurchargeBookingCalculationStrategy();
            case STANDARD:
            default:
                return new StandardBookingCalculationStrategy();
        }
    }
}

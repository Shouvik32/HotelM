package com.backendproject.hotel_system.Strategies.BookingExpenses;

import java.util.Date;

public interface ExpenseCalculationStrategy {
    public double calculateBookingExpense(double roomAmount, double serviceCharge, Date checkin, Date checkout);
}

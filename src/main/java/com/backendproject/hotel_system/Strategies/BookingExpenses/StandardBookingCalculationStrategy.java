package com.backendproject.hotel_system.Strategies.BookingExpenses;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StandardBookingCalculationStrategy implements  ExpenseCalculationStrategy{

    @Override
    public double calculateBookingExpense(double roomAmount, double serviceCharge, Date checkin, Date checkout){
        final double gst=roomAmount*0.18;
        long diffMillis = checkout.getTime() - checkin.getTime();
        long numberOfDays = diffMillis / (24 * 60 * 60 * 1000);
        return (roomAmount+serviceCharge==0?roomAmount*0.15:serviceCharge+gst)*numberOfDays;
    }

}

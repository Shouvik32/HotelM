package com.backendproject.hotel_system.Strategies.BookingExpenses;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StandardBookingCalculationStrategy implements  ExpenseCalculationStrategy{

    @Override
    public double calculateBookingExpense(double roomAmount, double serviceCharge, Date checkin, Date checkout) {
        final double gst = roomAmount * 0.18;
        long diffMillis = checkout.getTime() - checkin.getTime();
        long numberOfDays = Math.max(1, diffMillis / (24L * 60 * 60 * 1000));
        double appliedService = (serviceCharge <= 0.0) ? roomAmount * 0.15 : serviceCharge;
        double perDayTotal = roomAmount + appliedService + gst;
        System.out.printf("std days=%d room=%.2f serviceInput=%.2f appliedService=%.2f gst=%.2f perDay=%.2f%n",
                numberOfDays, roomAmount, serviceCharge, appliedService, gst, perDayTotal);
        return perDayTotal * numberOfDays;
    }

}

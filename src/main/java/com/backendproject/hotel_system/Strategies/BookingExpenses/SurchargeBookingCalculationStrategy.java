package com.backendproject.hotel_system.Strategies.BookingExpenses;


import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SurchargeBookingCalculationStrategy implements ExpenseCalculationStrategy{


    @Override
    public double calculateBookingExpense(double roomAmount, double serviceCharge, Date checkin, Date checkout) {
        final double gst = 0.18 * roomAmount;
        long diffMillis = checkout.getTime() - checkin.getTime();
        long numberOfDays = Math.max(1, java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffMillis));
        double totalBeforeGst =roomAmount+((serviceCharge <= 0.0) ? roomAmount  :   serviceCharge * 1.3) ;
        double perDayTotal = totalBeforeGst + gst;
        System.out.printf("surc days=%d room=%.2f serviceInput=%.2f totalBeforeGst=%.2f gst=%.2f perDay=%.2f%n",
                numberOfDays, roomAmount, serviceCharge, totalBeforeGst, gst, perDayTotal);
        return perDayTotal * numberOfDays;
    }
}

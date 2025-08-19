package com.backendproject.hotel_system.Strategies.BookingExpenses;


import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SurchargeBookingCalculationStrategy implements ExpenseCalculationStrategy{


    @Override
    public double calculateBookingExpense(double roomAmount, double serviceCharge, Date checkin, Date checkout){
        final double  gst=0.18*roomAmount;
        long diffMillis = checkout.getTime() - checkin.getTime();
        long numberOfDays = diffMillis / (24 * 60 * 60 * 1000);
         double surcharge=serviceCharge==0?roomAmount*0.3:roomAmount+serviceCharge+(0.3*(roomAmount+serviceCharge));
         return (roomAmount+surcharge+gst)*numberOfDays;
    }

}

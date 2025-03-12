package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Exceptions.CustomerSessionNotFoundException;
import com.backendproject.hotel_system.Models.Invoice;


public interface InvoiceServices {
    public Invoice generateInvoice(long userId) throws CustomerSessionNotFoundException;
}

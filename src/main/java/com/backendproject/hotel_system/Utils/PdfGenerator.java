package com.backendproject.hotel_system.Utils;

import com.backendproject.hotel_system.Dtos.Requests.BookingRequestDto;
import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.BookingRoom;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Strategies.BookingExpenses.BookingCalculationStrategyFactory;
import com.backendproject.hotel_system.Strategies.BookingExpenses.ExpenseCalculationStrategy;
import com.backendproject.hotel_system.Strategies.BookingExpenses.StrategyType;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public final class PdfGenerator {


    public static byte[] generateBookingPdf(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }

        // ---- Hotel / Customer info ----
        String hotelName = invoice.getHotel() != null ? nz(invoice.getHotel().getName()) : "-";
        String hotelAddress = (invoice.getHotel() != null && invoice.getHotel().getLocation() != null)
                ? nz(invoice.getHotel().getLocation().getAddress()) : "-";

        String guestName = "-";
        String phone = "-";
        if (invoice.getCustomer() != null) {
            String first = nz(invoice.getCustomer().getFirstName());
            String last  = nz(invoice.getCustomer().getLastName());
            guestName = (first + " " + last).trim();
            phone = nz(invoice.getCustomer().getPhone());
        }

        String checkInStr  = fmt(invoice.getCheckInDate());
        String checkOutStr = fmt(invoice.getCheckOutDate());
        long diffMillis = invoice.getCheckOutDate().getTime() - invoice.getCheckInDate().getTime();
        long numberOfDays = Math.max(1, java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffMillis));

        StringBuilder roomDetails = new StringBuilder("-");
        if (invoice.getBookings() != null && !invoice.getBookings().isEmpty()) {
            List<String> roomLines = invoice.getBookings().stream()
                    .filter(Objects::nonNull)
                    .flatMap(b -> b.getBookingRooms().stream())
                    .filter(Objects::nonNull)
                    .map(br -> {
                        String rn = (br.getRoom() != null) ? nz(br.getRoom().getRoomNumber()) : "-";
                        int count = br.getBookedRoomsCount() != null ? br.getBookedRoomsCount() : 1;
                        double price = br.getPrice();
                        return rn + " -> " + dbl(price * count);
                    })
                    .toList();

            if (!roomLines.isEmpty()) {
                roomDetails = new StringBuilder(String.join(", ", roomLines));
            }
        }

        double roomPrice     = invoice.getRoomPrice();
        double serviceCharge = invoice.getServiceCharge();
        double gst           = invoice.getGst();
        double totalAmount   = invoice.getTotalAmount();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph(hotelName)
                .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Hotel Address: " + hotelAddress)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));


        document.add(new Paragraph("Booking Invoice")
                .setBold().setFontSize(16));
        document.add(new Paragraph("\n"));


        float[] colWidths = {200F, 300F};
        Table table = new Table(UnitValue.createPointArray(colWidths));

        addRow(table, "Invoice ID",    String.valueOf(invoice.getId()));
        addRow(table, "Guest Name",    guestName);
        addRow(table, "Phone",         phone);
        addRow(table, "Check In Date",  checkInStr);
        addRow(table, "Check Out Date", checkOutStr);
        addRow(table, "Rooms",         roomDetails.toString());

        addRow(table, "Room Price per day",    dbl(roomPrice));
        addRow(table, "Service Charge",dbl(serviceCharge));
        addRow(table, "GST", (int)gst + "%");
        addRow(table, "No. of days staying",String.valueOf(numberOfDays));
        addRow(table, "Total before gst",dbl((serviceCharge+roomPrice)*numberOfDays));
        addRow(table, "Total Amount",  dbl(totalAmount));

        document.add(table);
        document.add(new Paragraph("\nThank you for choosing " + hotelName + "!")
                .setTextAlignment(TextAlignment.CENTER));

        document.close();
        return out.toByteArray();
    }
    private static void addRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label)).setBold());
        table.addCell(new Cell().add(new Paragraph(nz(value))));
    }

    private static String nz(String v) {
        return v == null ? "-" : v;
    }

    private static String fmt(Date d) {
        if (d == null) return "-";
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    private static String dbl(double v) {

        return String.format("%.2f", v);
    }
}

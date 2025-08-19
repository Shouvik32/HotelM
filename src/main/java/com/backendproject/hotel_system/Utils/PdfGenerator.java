package com.backendproject.hotel_system.Utils;

import com.backendproject.hotel_system.Dtos.Responses.BookingResponse;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

public class PdfGenerator {

    public static byte[] generateBookingPdf(BookingResponse bookingResponseDto) {
        System.out.println("Generating PDF for: " + bookingResponseDto.getUserName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(bookingResponseDto.getHotelName())
                .setBold()
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Hotel Address: " + bookingResponseDto.getAddress())
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Booking Invoice")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("\n"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkInStr = bookingResponseDto.getCheckIn() != null ? sdf.format(bookingResponseDto.getCheckIn()) : "";
        String checkOutStr = bookingResponseDto.getCheckOut() != null ? sdf.format(bookingResponseDto.getCheckOut()) : "";

        float[] columnWidths = {200F, 300F};
        Table table = new Table(UnitValue.createPointArray(columnWidths));

        table.addCell(new Cell().add(new Paragraph("Guest Name")).setBold());
        table.addCell(new Cell().add(new Paragraph(bookingResponseDto.getUserName())));

        table.addCell(new Cell().add(new Paragraph("Phone")).setBold());
        table.addCell(new Cell().add(new Paragraph(bookingResponseDto.getPhone())));

        table.addCell(new Cell().add(new Paragraph("Room Numbers")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.join(", ", bookingResponseDto.getRooms()))));

        table.addCell(new Cell().add(new Paragraph("Check In Date")).setBold());
        table.addCell(new Cell().add(new Paragraph(checkInStr)));

        table.addCell(new Cell().add(new Paragraph("Check Out Date")).setBold());
        table.addCell(new Cell().add(new Paragraph(checkOutStr)));

        table.addCell(new Cell().add(new Paragraph("Total Amount")).setBold());
        table.addCell(new Cell().add(new Paragraph(String.valueOf(bookingResponseDto.getTotalAmount()))));

        document.add(table);

        document.add(new Paragraph("\nThank you for choosing " + bookingResponseDto.getHotelName() + "!")
                .setTextAlignment(TextAlignment.CENTER));

        document.close();
        return out.toByteArray();
    }
}
package com.backendproject.hotel_system.Controllers;

import com.backendproject.hotel_system.Dtos.Requests.BookingRequestDto;
import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.Dtos.Responses.BookingResponse;
import com.backendproject.hotel_system.Strategies.BookingExpenses.StrategyType;
import com.backendproject.hotel_system.Utils.PdfGenerator;
import com.backendproject.hotel_system.Exceptions.*;
import com.backendproject.hotel_system.Models.*;
import com.backendproject.hotel_system.repositories.UserRepository;
import com.backendproject.hotel_system.services.BookingServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingServices bookingServices;
    private final UserRepository userRepository;

    @Autowired
    public BookingController(BookingServices bookingServices, UserRepository userRepository) {
        this.bookingServices = bookingServices;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BOOK_ROOM')")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@RequestBody BookingRequestDto request,@RequestParam String type) {
        try {
            System.out.println(StrategyType.valueOf(type)+"controller");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = Long.parseLong(authentication.getName());

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            Invoice invoice = bookingServices.bookRoom(user.getId(), request.getRoomIds(), request.getCheckIn(), request.getCheckOut(), StrategyType.valueOf(type));
            BookingResponse data = BookingResponse.fromInvoice(invoice, request.getCheckIn(), request.getCheckOut());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("success", "Booking created successfully", data));

        } catch (UserNotFoundException | RoomNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (CustomerSessionNotFoundException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RoomAlreadyBookedForDatesException e) {
            return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Booking failed: " + e.getMessage());
        }
    }

    @GetMapping("/invoice/{invoiceId}")
    @PreAuthorize("hasAuthority('BOOK_ROOM')")
    @Transactional
    public ResponseEntity<?> getInvoicePdf(@PathVariable Long invoiceId) {
        try {
            Invoice invoice = bookingServices.getInvoiceById(invoiceId);

            byte[] pdf = PdfGenerator.generateBookingPdf(invoice);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("booking_invoice_" + invoiceId + ".pdf")
                            .build()
            );

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF: " + e.getMessage());
        }
    }

    private ResponseEntity<ApiResponse<BookingResponse>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>("failure", message, null));
    }
}

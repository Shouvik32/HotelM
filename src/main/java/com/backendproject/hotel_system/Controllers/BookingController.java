package com.backendproject.hotel_system.Controllers;

import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.Dtos.Responses.BookingResponse;
import com.backendproject.hotel_system.Utils.PdfGenerator;
import com.backendproject.hotel_system.dto.BookingRequestDto;

import com.backendproject.hotel_system.Exceptions.CustomerSessionNotFoundException;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Exceptions.RoomAlreadyBookedForDatesException;
import com.backendproject.hotel_system.Exceptions.UserNotFoundException;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.User;
import com.backendproject.hotel_system.repositories.UserRepository;
import com.backendproject.hotel_system.services.BookingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @RequestBody BookingRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            long username = Long.parseLong(authentication.getName());
            User user = userRepository.findById(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Long userId = user.getId();

            Invoice invoice = bookingServices.bookRoom(
                    userId,
                    request.getRoomIds(),
                    request.getCheckIn(),
                    request.getCheckOut()
            );

            BookingResponse data = BookingResponse.fromInvoice(invoice, request.getCheckIn(), request.getCheckOut());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("success", "Booking created successfully", data));
        } catch (UserNotFoundException | RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("failure", e.getMessage(), null));
        } catch (CustomerSessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("failure", e.getMessage(), null));
        } catch (RoomAlreadyBookedForDatesException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.toString(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Booking failed: " + e.getMessage(), null));
        }
    }
    @GetMapping("/invoice/{bookingId}")
    @PreAuthorize("hasAuthority('BOOK_ROOM')")
    public ResponseEntity<byte[]> getBookingPdf(@PathVariable Long bookingId) {
        Invoice invoice = bookingServices.getInvoiceByBookingId(bookingId);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        BookingResponse bookingResponse = BookingResponse.fromInvoice(
                invoice,
                invoice.getBookingRooms().get(0).getBooking().getCheckInDate(),
                invoice.getBookingRooms().get(0).getBooking().getCheckOutDate()
        );
        byte[] pdfBytes = PdfGenerator.generateBookingPdf(bookingResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("booking_invoice_" + bookingId + ".pdf")
                        .build()
        );
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
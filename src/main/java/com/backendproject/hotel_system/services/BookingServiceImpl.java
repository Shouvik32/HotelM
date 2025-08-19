package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Exceptions.RoomAlreadyBookedForDatesException;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Exceptions.UserNotFoundException;
import com.backendproject.hotel_system.Models.Booking;
import com.backendproject.hotel_system.Models.BookingRoom;
import com.backendproject.hotel_system.Models.CustomerSession;
import com.backendproject.hotel_system.Models.CustomerSessionStatus;
import com.backendproject.hotel_system.Models.Invoice;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.User;
import com.backendproject.hotel_system.Strategies.BookingExpenses.BookingCalculationStrategyFactory;
import com.backendproject.hotel_system.Strategies.BookingExpenses.ExpenseCalculationStrategy;
import com.backendproject.hotel_system.Strategies.BookingExpenses.StrategyType;
import com.backendproject.hotel_system.repositories.BookingRepository;
import com.backendproject.hotel_system.repositories.BookingRoomRepository;
import com.backendproject.hotel_system.repositories.CustomerSessionRepository;
import com.backendproject.hotel_system.repositories.InvoiceRepository;
import com.backendproject.hotel_system.repositories.RoomRepository;
import com.backendproject.hotel_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingServices {

    private final CustomerSessionRepository customerSessionRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final InvoiceRepository invoiceRepository;
    private final BookingCalculationStrategyFactory expenseCalculationStrategy;

    @Autowired
    public BookingServiceImpl(
            CustomerSessionRepository customerSessionRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            BookingRepository bookingRepository,
            BookingRoomRepository bookingRoomRepository,
            InvoiceRepository invoiceRepository,
            BookingCalculationStrategyFactory expenseCalculationStrategy
    ) {
        this.customerSessionRepository = customerSessionRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.bookingRoomRepository = bookingRoomRepository;
        this.invoiceRepository = invoiceRepository;
        this.expenseCalculationStrategy = expenseCalculationStrategy;
    }

    @Override
    @Transactional
    public Invoice bookRoom(Long userId, List<Long> roomIds, Date checkIn, Date checkOut) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        CustomerSession session = customerSessionRepository.findFirstByUser_IdAndCustomerSessionStatus(
                userId, CustomerSessionStatus.ACTIVE
        ).orElseGet(() -> {
            CustomerSession newSession = new CustomerSession();
            newSession.setUser(user);
            newSession.setSessionStart(new Date());
            newSession.setCustomerSessionStatus(CustomerSessionStatus.ACTIVE);
            return customerSessionRepository.save(newSession);
        });

        List<Room> rooms = roomRepository.findAllById(roomIds);
        if (rooms.size() != roomIds.size()) {
            throw new RoomNotFoundException("Some rooms do not exist");
        }


        for (Room room : rooms) {
            boolean isBooked = bookingRoomRepository.existsOverlappingBookingForRoom(room, checkIn, checkOut);
            if (isBooked) {
                throw new RoomAlreadyBookedForDatesException("Room " + room.getId() + " is not available for the selected dates");
            }
        }

        Booking booking = new Booking();
        booking.setCustomerSession(session);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking = bookingRepository.save(booking);

        List<BookingRoom> bookingRooms = new ArrayList<>();
        double totalRoomAmount = 0.0;
        for (Room room : rooms) {
            BookingRoom br = new BookingRoom();
            br.setBooking(booking);
            br.setRoom(room);
            br.setBookedRoomsCount(1);
            br.setCheckInDate(checkIn);
            br.setCheckOutDate(checkOut);
            bookingRooms.add(br);
            totalRoomAmount += room.getPrice();
        }
        bookingRoomRepository.saveAll(bookingRooms);

        booking.setBookedRooms(bookingRooms);
        bookingRepository.save(booking);

        double serviceCharge = totalRoomAmount * 0.2;
        ExpenseCalculationStrategy strategy = expenseCalculationStrategy.getStrategy(StrategyType.STANDARD);
        double totalAmount = strategy.calculateBookingExpense(totalRoomAmount, serviceCharge,checkIn,checkOut);

        Invoice invoice = new Invoice();
        invoice.setBookingId(booking.getId());
        invoice.setBookedRooms(bookingRooms);
        invoice.setTotalAmount(Math.round(totalAmount*100.0)/100);
        invoice.setGst(18);
        invoice.setServiceCharge(serviceCharge);
        invoice.setCustomerSession(session);
        invoice = invoiceRepository.save(invoice);

        return invoice;
    }
    @Override
    public Invoice getInvoiceByBookingId(Long bookingId) {
        // Assuming each booking has one invoice. You can adjust if your model is different.
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        if (booking == null) return null;
        // If Invoice has booking reference, you can do:
        return (Invoice) invoiceRepository.findByBookingId(booking.getId()).get();
        // OR if Invoice is linked differently, you may need to search by session/rooms.
    }
}
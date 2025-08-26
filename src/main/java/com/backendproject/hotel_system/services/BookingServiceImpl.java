package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Exceptions.RoomAlreadyBookedForDatesException;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Exceptions.UserNotFoundException;
import com.backendproject.hotel_system.Models.*;
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

import java.util.*;
import java.util.stream.Collectors;

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
    public Invoice bookRoom(Long userId, List<Long> roomIds, Date checkIn, Date checkOut,StrategyType type) {
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
        booking.setHotel(rooms.get(0).getHotel());
        booking = bookingRepository.save(booking);
        List<BookingRoom> bookingRooms = new ArrayList<>();
        double totalRoomAmount = 0.0;
        int i=0;
        for (Room room : rooms) {
            i++;
            BookingRoom br = new BookingRoom();
            br.setBooking(booking);
            br.setRoom(room);
            br.setBookedRoomsCount(i);
            br.setCheckInDate(checkIn);
            br.setCheckOutDate(checkOut);
            br.setPrice(room.getPrice());
            bookingRooms.add(br);
            totalRoomAmount += room.getPrice();
            System.out.print(room.getPrice());
        }
        bookingRoomRepository.saveAll(bookingRooms);
        booking.setBookingRooms(bookingRooms);

        double serviceCharge = totalRoomAmount * 0.2;
        ExpenseCalculationStrategy strategy = expenseCalculationStrategy.getStrategy(type);
        double totalAmount = strategy.calculateBookingExpense(totalRoomAmount, serviceCharge, checkIn, checkOut);
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;
        Invoice invoice = invoiceRepository.findExistingInvoice(
                user.getId(),
                rooms.get(0).getHotel().getId(),
                checkIn,
                checkOut
        ).orElseGet(() -> {
            Invoice newInvoice = new Invoice();
            newInvoice.setCustomer(user);
            newInvoice.setHotel(rooms.get(0).getHotel());
            newInvoice.setCheckInDate(checkIn);
            newInvoice.setCheckOutDate(checkOut);
            newInvoice.setBookings(new ArrayList<>());
            newInvoice.setRoomPrice(0.0);
            newInvoice.setServiceCharge(0.0);
            newInvoice.setGst(18);
            newInvoice.setTotalAmount(0.0);
            return newInvoice;
        });

        invoice.getBookings().add(booking);
        booking.setInvoice(invoice);
        invoice.setRoomPrice(invoice.getRoomPrice() + totalRoomAmount);
        invoice.setServiceCharge(invoice.getServiceCharge() + serviceCharge);
        invoice.setTotalAmount(invoice.getTotalAmount() + totalAmount);

        invoice = invoiceRepository.save(invoice);
        bookingRepository.save(booking);

        return invoice;
    }


    @Override
    public Invoice getInvoiceByBookingId(Long bookingId) {
        Optional<Invoice> directInvoice = invoiceRepository.findByBookings_Id(bookingId);
        if (directInvoice.isPresent()) {
            return directInvoice.get();
        }
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            System.err.println("Booking not found: " + bookingId);
            return null;
        }
        if (booking.getInvoice() != null) {
            return booking.getInvoice();
        }

        User customer = booking.getCustomerSession() != null ? booking.getCustomerSession().getUser() : null;
        if (customer == null) {
            System.err.println("Warning: Booking " + bookingId + " has no associated customer");
            return null;
        }
        Hotel hotel = booking.getHotel();
        if (hotel == null) {
            System.err.println("Warning: Booking " + bookingId + " has no associated hotel");
            return null;
        }
        Date bookingIn = truncateToDate(booking.getCheckInDate());
        Date bookingOut = truncateToDate(booking.getCheckOutDate());


        Optional<Invoice> match = invoiceRepository.findByCustomerAndHotelAndCheckInDateAndCheckOutDate(
                customer, hotel, bookingIn, bookingOut
        );

        if (match.isPresent()) {
            Invoice invoice = match.get();
            boolean bookingExists = invoice.getBookings().stream()
                    .anyMatch(b -> b.getId().equals(booking.getId()));

            if (!bookingExists) {
                invoice.getBookings().add(booking);
                booking.setInvoice(invoice);
                bookingRepository.save(booking); // Save the booking with updated invoice reference
            }

            double totalRoomPrice = invoice.getBookings().stream()
                    .flatMap(b -> b.getBookingRooms().stream())
                    .mapToDouble(br -> br.getPrice())
                    .sum();

            double serviceCharge = totalRoomPrice * 0.2;
            ExpenseCalculationStrategy strategy = expenseCalculationStrategy.getStrategy(StrategyType.STANDARD);
            double totalAmount = strategy.calculateBookingExpense(totalRoomPrice, serviceCharge, bookingIn, bookingOut);

            invoice.setRoomPrice(totalRoomPrice);
            invoice.setServiceCharge(serviceCharge);
            invoice.setTotalAmount(Math.round(totalAmount * 100.0) / 100.0);

            return invoiceRepository.save(invoice);
        }

        System.err.println("No invoice found for booking: " + bookingId);
        return null;
    }
    @Transactional
    public Invoice getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + invoiceId));
        invoice.getBookings().forEach(b -> b.getBookingRooms().size());

        return invoice;
    }
    public Booking getBookingById(Long bookingId){
        return bookingRepository.findById(bookingId).orElse(null);
    }
    private Date truncateToDate(Date d) {
        if (d == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
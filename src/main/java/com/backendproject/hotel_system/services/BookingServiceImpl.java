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
            System.out.println(room.getPrice()+"price of each"+totalRoomAmount);
        }
        bookingRoomRepository.saveAll(bookingRooms);

        booking.setBookingRooms(bookingRooms);
        bookingRepository.save(booking);

        double serviceCharge = totalRoomAmount * 0.2;
        ExpenseCalculationStrategy strategy = expenseCalculationStrategy.getStrategy(StrategyType.STANDARD);
        double totalAmount = strategy.calculateBookingExpense(totalRoomAmount, serviceCharge,checkIn,checkOut);

        Invoice invoice = new Invoice();
        invoice.setBookingId(booking.getId());
        invoice.setBookingRooms(bookingRooms);
        invoice.setTotalAmount(Math.round(totalAmount*100.0)/100);
        invoice.setGst(18);
        invoice.setServiceCharge(serviceCharge);
        invoice.setCustomerSession(session);
        invoice = invoiceRepository.save(invoice);

        return invoice;
    }
    @Override
    public Invoice getInvoiceByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) return null;

        Long customerSessionId = booking.getCustomerSession() != null ? booking.getCustomerSession().getId() : null;
        Long bookingHotelId = booking.getHotel() != null ? booking.getHotel().getId() : null;

        Date bookingIn = truncateToDate(booking.getCheckInDate());
        Date bookingOut = truncateToDate(booking.getCheckOutDate());

        List<Invoice> candidates;
        if (customerSessionId != null) {
            candidates = invoiceRepository.findByCustomerSessionId(customerSessionId);
        } else {
            candidates = invoiceRepository.findAll();
        }

        Optional<Invoice> match = candidates.stream()
                .filter(inv -> inv.getCustomerSession().getSessionStart() != null && inv.getCustomerSession().getSessionEnd() != null)
                .filter(inv -> Objects.equals(truncateToDate(inv.getCustomerSession().getSessionStart()), bookingIn))
                .filter(inv -> Objects.equals(truncateToDate(inv.getCustomerSession().getSessionEnd()), bookingOut))
                .filter(inv -> Objects.equals(inv.getHotel() != null ? inv.getHotel().getId() : null, bookingHotelId))
                .findFirst();

        if (match.isPresent()) {
            Invoice invoice = match.get();

            List<BookingRoom> bookingRooms = booking.getBookingRooms() != null ? booking.getBookingRooms() : Collections.emptyList();
            List<BookingRoom> invoiceRooms = invoice.getBookingRooms() != null ? invoice.getBookingRooms() : new ArrayList<>();

            Set<Long> invoiceRoomIds = invoiceRooms.stream()
                    .map(BookingRoom::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (BookingRoom br : bookingRooms) {
                if (br == null) continue;
                Long brId = br.getId();
                if (brId == null || !invoiceRoomIds.contains(brId)) {
                    invoiceRooms.add(br);
                    if (brId != null) invoiceRoomIds.add(brId);
                }
            }

            invoice.setBookingRooms(invoiceRooms);
            invoiceRepository.save(invoice);
            return invoice;
        }

        return invoiceRepository.findByBookingId(booking.getId()).get();
    }
    private Date truncateToDate(Date d) {
        if (d == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
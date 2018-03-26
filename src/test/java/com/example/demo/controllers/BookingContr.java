package com.example.demo.controllers;


import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.TypeBookingService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookingContr{
    private TypeBookingService typeBookingService;
    private BookingService bookingService;
    private DocumentService documentService;
    private UserService userService;

    private static final long BESTSELLER_FOR_PATRON_TIME = 1209600000L;

    private static final long PATRON_DEFAULT_TIME = 1814400000L;

    private static final long FACULTY_DEFAULT_TIME = 2419200000L;

    private static final long AV_JOURNAL_TIME = 1209600000L;

    BookingContr(BookingService bookingService, DocumentService documentService, UserService userService, TypeBookingService typeBookingService) {
        this.bookingService = bookingService;
        this.documentService = documentService;
        this.userService = userService;
        this.typeBookingService = typeBookingService;
    }

    public List<Booking> findBookingByUserIdTest(Integer id) {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findMyBookingTest(Integer id) {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findReturnBooksTest() {
        return bookingService.findAll()
                .stream()
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
                .filter(booking -> "return request".equals(booking.getTypeBooking().getTypeName()))
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findAllBookingsTest() {
        return bookingService.findAll();
    }

    public void requestDocumentByIdTest(Integer documentId, User user) {
        Date returnDate = new Date();
        if (user == null)
            throw new UserNotFoundException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
        if (!document.isReference() && document.getCount() > 0) {
            long time = System.currentTimeMillis();
            if(document.getType().getTypeName().equals("book")){
                if (user.getRole().getName().equals("patron")){
                    if (document.isBestseller()){
                        returnDate.setTime(time + BESTSELLER_FOR_PATRON_TIME);
                    }else{
                        returnDate.setTime(time + PATRON_DEFAULT_TIME);
                    }
                }
                if (user.getRole().getName().equals("faculty")){
                    returnDate.setTime(time + FACULTY_DEFAULT_TIME);
                }
            }
            else{
                returnDate.setTime(time + AV_JOURNAL_TIME);
            }
            bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("taken")));
            document.setCount(document.getCount() - 1);
            documentService.save(document);
        }
        else{
            throw new AccessDeniedException();
        }
    }

    public void returnDocumentByIdTest(Integer id){
        if (id == null)
            throw new NullIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return request"));
        bookingService.save(booking);
    }

    public void closeBookingTest(Integer id){
        Booking booking = bookingService.getBookingById(id);
        if(booking == null){
            throw new BookingNotFoundException();
        }
        booking.setTypeBooking(typeBookingService.findByTypeName("close"));
        bookingService.save(booking);
        Document document = booking.getDocument();
        document.setCount(document.getCount() + 1);
        documentService.save(document);
    }

    public void removeBookingTest(Booking booking){
        this.bookingService.removeBookingById(booking.getId());
    }

    public void removeBookingById(Integer id){
        this.bookingService.removeBookingById(id);
    }
}

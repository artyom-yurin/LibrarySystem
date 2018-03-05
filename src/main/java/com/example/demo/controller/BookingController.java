package com.example.demo.controller;


import com.example.demo.entity.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;
import com.example.demo.exception.DocumentNotFoundException;
import com.example.demo.exception.NullIdException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private BookingService bookingService;
    private DocumentService documentService;
    private UserService userService;

    BookingController(BookingRepository bookingRepository, DocumentService documentService, UserService userService) {
        bookingService = new BookingService(bookingRepository);
        this.documentService = documentService;
        this.userService = userService;
    }

    @GetMapping("/booking/find")
    public Iterable<Booking> findBookingByUserId(@RequestParam(name = "id", defaultValue = "-1") Integer id) {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !booking.isHasBackRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findback")
    public Iterable<Booking> findReturnBooks() {
        return bookingService.findAll()
                .stream()
                .filter(booking -> !booking.isClose())
                .filter(booking -> booking.isHasBackRequest())
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findall")
    public Iterable<Booking> findAllBookings() {
        return bookingService.findAll();
    }

    @PostMapping("/booking/request")
    public void requestDocumentById(@RequestParam(value = "id", defaultValue = "") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new NullIdException();//TODO: TOKEN EXCEPTION
        if (id == null)
            throw new NullIdException();
        Document document = documentService.findById(id);
        if (document == null)
            throw new DocumentNotFoundException();
        if (token.id == null)
            throw new NullIdException();
        User user = userService.findById(token.id);
        if (user == null)
            throw new UserNotFoundException();
        if (!document.isReference() && document.getCount() > 0) {
            bookingService.save(new Booking(user, document, null, false, 0, false));
        }
        else{
            throw new DocumentNotFoundException();//TODO: REFERENCE/NO BOOK EXCEPTION
        }
    }

    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam Integer id, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new NullIdException();//TODO: TOKEN EXCEPTION
        if (id == null)
            throw new NullIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new NullIdException();//TODO: BOOKING EXCEPTION
        booking.setHasBackRequest(true);
        bookingService.save(booking);
    }

    @PutMapping("/booking/close")
    public void closeBooking(@RequestParam Integer id){
        Booking booking = bookingService.getBookingById(id);
        if(booking == null){
            throw new NullIdException();//TODO: BOOKING EXCEPTION
        }
        booking.setClose(true);
        bookingService.save(booking);
    }

    @Transactional
    @DeleteMapping("/booking/remove")
    public void removeBooking(@RequestBody Booking booking){
        this.bookingService.removeBookingById(booking.getId());
    }

    @Transactional
    @DeleteMapping("/booking/idremove")
    public void removeBookingById(@RequestParam Integer id){
        this.bookingService.removeBookingById(id);
    }
}

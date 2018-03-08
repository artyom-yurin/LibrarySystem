package com.example.demo.controllers;


import com.example.demo.entity.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookingContr{
    private BookingService bookingService;
    private DocumentService documentService;
    private UserService userService;

    BookingContr(BookingRepository bookingRepository, DocumentService documentService, UserService userService) {
        bookingService = new BookingService(bookingRepository);
        this.documentService = documentService;
        this.userService = userService;
    }

    public List<Booking> findBookingByUserIdTest(Integer id) {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !booking.isHasBackRequest())
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findMyBookingTest(Integer id) {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !booking.isHasBackRequest())
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findReturnBooksTest() {
        return bookingService.findAll()
                .stream()
                .filter(booking -> !booking.isClose())
                .filter(booking -> booking.isHasBackRequest())
                .collect(Collectors.toList());
    }

    public Iterable<Booking> findAllBookingsTest() {
        return bookingService.findAll();
    }

    public void requestDocumentByIdTest(Integer documentId, Integer userId) {
        if (documentId == null)
            throw new NullIdException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
        if (userId == null)
            throw new NullIdException();
        User user = userService.findById(userId);
        if (user == null)
            throw new UserNotFoundException();
        if (!document.isReference() && document.getCount() > 0) {
            Booking booking = new Booking(user, document, null, false, 0, false);
            booking.setId(-1);
            bookingService.save(booking);
            document.setCount(document.getCount() - 1);
            documentService.save(document);
        }
        else{
            throw new AccessDeniedException();
        }
    }

    public void requestDocumentByIdTest(Integer documentId, User user) {
        if (user == null)
            throw new UserNotFoundException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
        if (!document.isReference() && document.getCount() > 0) {
            Booking booking = new Booking(user, document, null, false, 0, false);
            booking.setId(-1);
            bookingService.save(booking);
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
        booking.setHasBackRequest(true);
        bookingService.save(booking);
    }

    public void closeBookingTest(Integer id){
        Booking booking = bookingService.getBookingById(id);
        if(booking == null){
            throw new BookingNotFoundException();
        }
        booking.setClose(true);
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

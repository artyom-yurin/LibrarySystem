package com.example.demo.controller;

import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.TypeBookingService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private TypeBookingService typeBookingService;
    private BookingService bookingService;
    private DocumentService documentService;
    private UserService userService;

    private static final long BESTSELLER_FOR_PATRON_TIME = 1209600000L;

    private static final long PATRON_DEFAULT_TIME = 1814400000L;

    private static final long FACULTY_DEFAULT_TIME = 2419200000L;

    private static final long AV_JOURNAL_TIME = 1209600000L;

    BookingController(BookingService bookingService, DocumentService documentService, UserService userService, TypeBookingService typeBookingService) {
        this.bookingService = bookingService;
        this.documentService = documentService;
        this.userService = userService;
        this.typeBookingService = typeBookingService;
    }

    @GetMapping("/booking/find")
    public Iterable<Booking> findBookingByUserId(@RequestParam(name = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !("is_close".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findself")
    public Iterable<Booking> findMyBooking(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(token.id))
                .filter(booking -> !("is_close".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findback")
    public Iterable<Booking> findReturnBooks(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        return bookingService.findAll()
                .stream()
                .filter(booking -> !"is_close".equals(booking.getTypeBooking().getTypeName()))
                .filter(booking -> "return_request".equals(booking.getTypeBooking().getTypeName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findall")
    public Iterable<Booking> findAllBookings(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return bookingService.findAll();
    }

    @PostMapping("/booking/request")
    public void requestDocumentById(@RequestParam(value = "id", defaultValue = "") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        Date returnDate = new Date();
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        if (documentId == null)
            throw new NullIdException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
        if (token.id == null)
            throw new NullIdException();
        User user = userService.findById(token.id);
        if (user == null)
            throw new UserNotFoundException();
        if (!document.isReference() && document.getCount() > 0) {
            long time = System.currentTimeMillis();
            if(document.getType().getTypeName().equals("book")){
                if (token.role.equals("patron")){
                    if (document.isBestseller()){
                        returnDate.setTime(time + BESTSELLER_FOR_PATRON_TIME);
                    }else{
                        returnDate.setTime(time + PATRON_DEFAULT_TIME);
                    }
                }
                if (token.role.equals("faculty")){
                    returnDate.setTime(time + FACULTY_DEFAULT_TIME);
                }
            }
            else{
                returnDate.setTime(time + AV_JOURNAL_TIME);
            }
            bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("book_taken")));
            document.setCount(document.getCount() - 1);
            documentService.save(document);
        }
        else{
            throw new AccessDeniedException();
        }
    }

    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam Integer id, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        if (id == null)
            throw new NullIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return_request"));
        bookingService.save(booking);
    }

    @PutMapping("/booking/close")
    public void closeBooking(@RequestParam Integer id, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        Booking booking = bookingService.getBookingById(id);
        if(booking == null){
            throw new BookingNotFoundException();
        }
        booking.setTypeBooking(typeBookingService.findByTypeName("is_close"));
        bookingService.save(booking);
        Document document = booking.getDocument();
        document.setCount(document.getCount() + 1);
        documentService.save(document);
    }

    @PutMapping("/booking/update")
    public void updateFine(HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        Date current = new Date();
        current.setTime(System.currentTimeMillis());

        for(Booking booking : bookingService.findAll()){
            if(current.getTime() - booking.getReturnDate().getTime() < 0)
                booking.setFine(0);

            Document document = booking.getDocument();

            int fine = Math.toIntExact((current.getTime() - booking.getReturnDate().getTime())/86400000)*100;
            if(fine > document.getPrice())
                booking.setFine(document.getPrice());
            else if(fine < 0)
                booking.setFine(0);
            else
                booking.setFine(fine);
            this.bookingService.save(booking);
        }
    }

    @Transactional
    @DeleteMapping("/booking/remove")
    public void removeBooking(@RequestBody Booking booking, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        this.bookingService.removeBookingById(booking.getId());
    }

    @Transactional
    @DeleteMapping("/booking/idremove")
    public void removeBookingById(@RequestParam Integer id, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        this.bookingService.removeBookingById(id);
    }
}

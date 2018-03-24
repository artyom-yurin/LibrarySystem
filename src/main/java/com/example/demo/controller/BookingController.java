package com.example.demo.controller;

import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.TypeBookingService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

    private static final long RENEW_TIME = 1209600000L;

    private static final long AVAILABLE_TIME = 86400000L;


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
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
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
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findback")
    public Iterable<Booking> findReturnBooks(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        return bookingService.findAll()
                .stream()
                .filter(booking -> !"close".equals(booking.getTypeBooking().getTypeName()))
                .filter(booking -> "return request".equals(booking.getTypeBooking().getTypeName()))
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
        if (!document.isReference()) {
            Date returnDate = new Date();
            if (document.getCount() > 0) {
                long time = System.currentTimeMillis();
                returnDate.setTime(time + AVAILABLE_TIME);
                bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("available")));
            } else {
                bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("open")));
            }
        } else {
            throw new AccessDeniedException();
        }
    }

    @PutMapping("/booking/take")
    public void takeDocumentByBookingId(@RequestParam Integer bookingId, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException();
        }

        if ("available".equals(booking.getTypeBooking().getTypeName()))
        {
            throw new AccessDeniedException();
        }

        Document document = booking.getDocument();
        User user = booking.getUser();
        Date returnDate = new Date();
        long time = System.currentTimeMillis();
        if (document.getType().getTypeName().equals("book")) {
            if (user.getRole().getName().equals("patron")) {
                if (document.isBestseller()) {
                    returnDate.setTime(time + BESTSELLER_FOR_PATRON_TIME);
                } else {
                    returnDate.setTime(time + PATRON_DEFAULT_TIME);
                }
            }
            if (user.getRole().getName().equals("faculty")) {
                returnDate.setTime(time + FACULTY_DEFAULT_TIME);
            }
        } else {
            returnDate.setTime(time + AV_JOURNAL_TIME);
        }
        document.setCount(document.getCount() - 1);
        documentService.save(document);
    }

    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        if (id == null)
            throw new NullIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return request"));
        bookingService.save(booking);
    }

    @PutMapping("/booking/close")
    public void closeBooking(@RequestParam Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new BookingNotFoundException();
        }
        booking.setTypeBooking(typeBookingService.findByTypeName("close"));
        bookingService.save(booking);


        Document document = booking.getDocument();
        PriorityQueue<Booking> priorityQueue = getQueueForBookById(document.getId());

        if (priorityQueue.size() > 0)
        {
            booking = priorityQueue.remove();
            booking.setTypeBooking(typeBookingService.findByTypeName("available"));
            Date returnData = new Date();
            returnData.setTime(System.currentTimeMillis() + AVAILABLE_TIME);
            booking.setReturnDate(returnData);
            bookingService.save(booking);
        }
        else
        {
            document.setCount(document.getCount() + 1);
            documentService.save(document);
        }
    }

    @PutMapping("/booking/update")
    public void updateFine(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        Date current = new Date();
        current.setTime(System.currentTimeMillis());

        for (Booking booking : bookingService.findAll()) {
            if (current.getTime() - booking.getReturnDate().getTime() < 0)
                booking.setFine(0);

            Document document = booking.getDocument();

            int fine = Math.toIntExact((current.getTime() - booking.getReturnDate().getTime()) / 86400000) * 100;
            if (fine > document.getPrice())
                booking.setFine(document.getPrice());
            else if (fine < 0)
                booking.setFine(0);
            else
                booking.setFine(fine);
            this.bookingService.save(booking);
        }
    }

    @Transactional
    @DeleteMapping("/booking/remove")
    public void removeBooking(@RequestBody Booking booking, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        this.bookingService.removeBookingById(booking.getId());
    }

    @Transactional
    @DeleteMapping("/booking/idremove")
    public void removeBookingById(@RequestParam Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        this.bookingService.removeBookingById(id);
    }

    @PutMapping("/booking/renew")
    public void renewBook(@RequestParam Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        Booking booking = bookingService.getBookingById(id);

        if (booking == null)
        {
            throw new BookingNotFoundException();
        }

        if (booking.getDocument().isBestseller())
        {
            throw new UnableRenewBestsellerException();
        }

        if ("renew".equals(booking.getTypeBooking().getTypeName()))
        {
            throw new AlreadyRenewException();
        }

        PriorityQueue<Booking> queue = getQueueForBookById(booking.getDocument().getId());

        if (queue.size() > 0)
        {
            throw new UnableRenewException();
        }

        booking.setTypeBooking(typeBookingService.findByTypeName("renew"));
        booking.setReturnDate(new Date(booking.getReturnDate().getTime() + RENEW_TIME));
        bookingService.save(booking);
    }

    public enum Positions {
        PROFESSOR, TA, INSTRUCTOR, STUDENT
    }

    private class MyComparator implements Comparator<Booking> {
        public int compare(Booking x, Booking y) {
            return convertToEnum(y.getUser().getRole().getPosition()).compareTo(convertToEnum(x.getUser().getRole().getPosition()));
        }
    }

    private Positions convertToEnum(String position)
    {
        switch (position.toLowerCase())
        {
            case "student": return Positions.STUDENT;
            case "instructor": return Positions.INSTRUCTOR;
            case "ta": return Positions.TA;
            case "professor": return Positions.PROFESSOR;
        }
        throw new RoleNotFoundException();
    }

    private PriorityQueue<Booking> getQueueForBookById(Integer bookId) {
        PriorityQueue<Booking> queue = new PriorityQueue<>(new MyComparator());


        queue.addAll(bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> "open".equals(booking.getTypeBooking().getTypeName()))
                .collect(Collectors.toList()));

        return queue;
    }
}

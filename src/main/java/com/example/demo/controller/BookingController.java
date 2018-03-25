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

import javax.persistence.criteria.CriteriaBuilder;
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

    private static final long VP_TIME = 604800000L;


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
        if (id == -1)
            throw new InvalidIdException();
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

    @GetMapping("/booking/findavailable")
    public Iterable<Booking> findAvailableBookings(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return bookingService.findAll();
    }

    @PostMapping("/booking/request")
    public void requestDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        if (documentId == -1)
            throw new InvalidIdException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
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
    public void takeDocumentByBookingId(@RequestParam(value = "id", defaultValue = "-1") Integer bookingId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        if (bookingId == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException();
        }

        if ("available".equals(booking.getTypeBooking().getTypeName())) {
            throw new AccessDeniedException();
        }

        Document document = booking.getDocument();
        User user = booking.getUser();
        Date returnDate = new Date();
        long time = System.currentTimeMillis();
        if (user.getRole().getName().equals("vp")) {
            returnDate.setTime(time + VP_TIME);
        } else if (document.getType().getTypeName().equals("book")) {
            if (user.getRole().getName().equals("patron")) {
                if (document.isBestseller()) {
                    returnDate.setTime(time + BESTSELLER_FOR_PATRON_TIME);
                } else {
                    returnDate.setTime(time + PATRON_DEFAULT_TIME);
                }
            } else if (user.getRole().getName().equals("faculty")) {
                returnDate.setTime(time + FACULTY_DEFAULT_TIME);
            }
        } else {
            returnDate.setTime(time + AV_JOURNAL_TIME);
        }
        document.setCount(document.getCount() - 1);
        documentService.save(document);
    }

    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        if (id == -1)
            throw new InvalidIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return request"));
        bookingService.save(booking);
    }

    @PutMapping("/booking/close")
    public void closeBooking(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            throw new BookingNotFoundException();
        }
        booking.setTypeBooking(typeBookingService.findByTypeName("close"));
        bookingService.save(booking);


        Document document = booking.getDocument();
        PriorityQueue<Booking> priorityQueue = getQueueForBookById(document.getId());

        if (priorityQueue.size() > 0) {
            booking = priorityQueue.remove();
            booking.setTypeBooking(typeBookingService.findByTypeName("available"));
            Date returnData = new Date();
            returnData.setTime(System.currentTimeMillis() + AVAILABLE_TIME);
            booking.setReturnDate(returnData);
            bookingService.save(booking);
            //TODO: NOTIFICATION TO NEW USER THAT BOOK AVAILABLE FOR HIM
        } else {
            document.setCount(document.getCount() + 1);
            documentService.save(document);
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
    public void removeBookingById(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        if (id == -1)
            throw new InvalidIdException();
        this.bookingService.removeBookingById(id);
    }

    @PutMapping("booking/outstanding")
    public void makeOutstandingRequest(@RequestParam(value = "id", defaultValue = "-1") Integer bookingId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        if (bookingId == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException();
        }

        PriorityQueue<Booking> priorityQueue = getQueueForBookById(booking.getDocument().getId());

        Booking firstBooking = priorityQueue.peek();

        if ("outstanding".equals(firstBooking.getTypeBooking().getTypeName()))
            throw new AlreadyHaveOutstandingRequestException();

        for (Booking bookItem : priorityQueue) {
            bookItem.setTypeBooking(typeBookingService.findByTypeName("close"));
            bookingService.save(bookItem);
            //TODO: NOTIFICATION TO ALL USER IN QUEUE THAT THEIR SPACE IN QUEUE IS CANCELED
        }
        booking.setTypeBooking(typeBookingService.findByTypeName("outstanding"));
        bookingService.save(booking);
    }

    @PutMapping("/booking/renew")
    public void renewBook(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(id);

        if (booking == null) {
            throw new BookingNotFoundException();
        }

        if (booking.getDocument().isBestseller()) {
            throw new UnableRenewBestsellerException();
        }

        if (!"vp".equals(booking.getUser().getRole().getName()) && "renew".equals(booking.getTypeBooking().getTypeName())) {
            throw new AlreadyRenewException();
        }

        PriorityQueue<Booking> queue = getQueueForBookById(booking.getDocument().getId());


        if ("outstanding".equals(queue.peek().getTypeBooking().getTypeName())) {
            throw new UnableRenewException();
        }

        booking.setTypeBooking(typeBookingService.findByTypeName("renew"));
        if ("vp".equals(booking.getUser().getRole().getName())) {
            booking.setReturnDate(new Date(booking.getReturnDate().getTime() + VP_TIME));
        } else {
            booking.setReturnDate(new Date(booking.getReturnDate().getTime() + RENEW_TIME));
        }
        bookingService.save(booking);
    }

    public List<Booking> findActiveBookings() {
        return bookingService.findAll()
                .stream()
                .filter(booking -> ("available".equals(booking.getTypeBooking().getTypeName())
                        || "taken".equals(booking.getTypeBooking().getTypeName())
                        || "renew".equals(booking.getTypeBooking().getTypeName())
                        || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    public void applyMeasures(Booking booking) {
        if ("available".equals(booking.getTypeBooking().getTypeName())) {
            booking.setTypeBooking(typeBookingService.findByTypeName("close"));
            bookingService.save(booking);
            //TODO NOTIFICATION THAT USER LOST HIM SPACE OF QUEUE
            PriorityQueue<Booking> pq = getQueueForBookById(booking.getDocument().getId());
            if (pq.size() > 0) {
                Booking newBooking = pq.peek();
                newBooking.setTypeBooking(typeBookingService.findByTypeName("available"));
                Date returnData = new Date();
                returnData.setTime(System.currentTimeMillis() + AVAILABLE_TIME);
                newBooking.setReturnDate(returnData);
                bookingService.save(newBooking);
                //TODO: NOTIFICATION TO NEW USER THAT BOOK AVAILABLE FOR HIM
            }
        } else {
            getFine(booking);
            //TODO: NOTIFICATION TO USER THAT HE HAS A FINE
        }
    }

    private void getFine(Booking booking) {
        Date current = new Date();
        current.setTime(System.currentTimeMillis());

        Document document = booking.getDocument();

        int fine = Math.toIntExact((current.getTime() - booking.getReturnDate().getTime()) / 86400000) * 100;
        if (fine > document.getPrice())
            booking.setFine(document.getPrice());
        else
            booking.setFine(fine);
        bookingService.save(booking);
    }

    public void queueAllocation(Integer bookID) {
        PriorityQueue<Booking> pq = getQueueForBookById(bookID);

        Document document = documentService.findById(bookID);
        Integer countDocument = document.getCount();

        for (int i = 0; i < countDocument && i < pq.size(); i++) {
            Booking booking = pq.poll();
            booking.setTypeBooking(typeBookingService.findByTypeName("available"));
            booking.setReturnDate(new Date(System.currentTimeMillis() + AVAILABLE_TIME));
            bookingService.save(booking);
            //TODO: NOTIFICATION TO NEW USER THAT BOOK AVAILABLE FOR HIM
            document.setCount(document.getCount() - 1);
            documentService.save(document);
        }
    }

    public enum Positions {
        PROFESSOR, VP, TA, INSTRUCTOR, STUDENT
    }

    private class MyComparator implements Comparator<Booking> {
        public int compare(Booking x, Booking y) {
            return convertToEnum(y.getUser().getRole().getPosition()).compareTo(convertToEnum(x.getUser().getRole().getPosition()));
        }
    }

    private Positions convertToEnum(String position) {
        switch (position.toLowerCase()) {
            case "student":
                return Positions.STUDENT;
            case "instructor":
                return Positions.INSTRUCTOR;
            case "ta":
                return Positions.TA;
            case "professor":
                return Positions.PROFESSOR;
            case "vp":
                return Positions.VP;
        }
        throw new RoleNotFoundException();
    }

    private PriorityQueue<Booking> getQueueForBookById(Integer bookId) {
        PriorityQueue<Booking> queue = new PriorityQueue<>(new MyComparator());

        queue.addAll(bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> ("open".equals(booking.getTypeBooking().getTypeName()) || "outstanding".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList()));

        return queue;
    }
}

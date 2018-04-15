package com.example.demo.controller;

import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.service.*;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import javafx.geometry.Pos;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private TypeBookingService typeBookingService;
    private BookingService bookingService;
    private DocumentService documentService;
    private UserService userService;
    private NotificationService notificationService;
    private LogService logService;

    private static final long BESTSELLER_FOR_PATRON_TIME = 1209600000L;

    private static final long PATRON_DEFAULT_TIME = 1814400000L;

    private static final long FACULTY_DEFAULT_TIME = 2419200000L;

    private static final long AV_JOURNAL_TIME = 1209600000L;

    private static final long RENEW_TIME = 1209600000L;

    private static final long AVAILABLE_TIME = 86400000L;

    private static final long DAY_TIME = 86400000L;

    private static final long VP_TIME = 604800000L;

    private static final long WEEK_AFTER_END = 604800000L;


    BookingController(BookingService bookingService, DocumentService documentService, UserService userService, TypeBookingService typeBookingService, NotificationService notificationService, LogService logService) {
        this.bookingService = bookingService;
        this.documentService = documentService;
        this.userService = userService;
        this.typeBookingService = typeBookingService;
        this.notificationService = notificationService;
        this.logService = logService;
    }

    @GetMapping("/booking/find")
    public Iterable<Booking> findBookingByUserId(@RequestParam(name = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
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
        if (!(token.role.equals("patron") || token.role.equals("faculty"))) throw new AccessDeniedException();
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(token.id))
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName()) || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    @GetMapping("/booking/findback")
    public Iterable<Booking> findReturnBooks(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();

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
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        return bookingService.findAll();
    }

    @GetMapping("/booking/findavailable")
    public Iterable<Booking> findAvailableBookings(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        return bookingService.findAll()
                .stream()
                .filter(booking -> ("available".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    @PostMapping("/booking/request")
    public void requestDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty"))) throw new AccessDeniedException();
        if (documentId == -1)
            throw new InvalidIdException();
        Document document = documentService.findById(documentId);
        if (document == null)
            throw new DocumentNotFoundException();
        User user = userService.findById(token.id);
        if (user == null)
            throw new UserNotFoundException();
        if (!document.isReference()) {
            List<Booking> myBookings = bookingService.findAll()
                    .stream()
                    .filter(booking -> booking.getUser().getId().equals(token.id))
                    .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName())))
                    .collect(Collectors.toList());
            for (Booking myBooking : myBookings)
            {
                if (myBooking.getDocument().getTitle().equals(document.getTitle()))
                {
                    throw new AccessDeniedException();
                }
            };
            Date returnDate = new Date();
            if (document.getCount() > 0) {
                long time = System.currentTimeMillis();
                returnDate.setTime(time + AVAILABLE_TIME);
                bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("available")));
                document.setCount(document.getCount() - 1);
                documentService.save(document);
            } else {
                bookingService.save(new Booking(user, document, returnDate, 0, typeBookingService.findByTypeName("open")));
            }
            logService.newLog(token.id, "Check out " + document.getTitle());
        } else {
            throw new AccessDeniedException();
        }
    }

    @PutMapping("/booking/take")
    public void takeDocumentByBookingId(@RequestParam(value = "id", defaultValue = "-1") Integer bookingId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();

        if (bookingId == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException();
        }

        if (!"available".equals(booking.getTypeBooking().getTypeName())) {
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
        booking.setTypeBooking(typeBookingService.findByTypeName("taken"));
        booking.setReturnDate(returnDate);
        bookingService.save(booking);
        logService.newLog(token.id, "Confirm that " + user.getUsername() + " taken " + document.getTitle());
    }

    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty"))) throw new AccessDeniedException();
        if (id == -1)
            throw new InvalidIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return request"));
        bookingService.save(booking);

        logService.newLog(token.id, "Want return " + booking.getDocument().getTitle());
    }

    @PutMapping("/booking/close")
    public void closeBooking(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();

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

            String message = "";
            message += document.getTitle();
            message += " is available for you";
            notificationService.newNotification(booking.getUser().getId(), message);
        } else {
            document.setCount(document.getCount() + 1);
            documentService.save(document);
        }


        logService.newLog(token.id, "Confirm that " + booking.getUser().getUsername() + " return " + document.getTitle());
    }

    @Transactional
    @DeleteMapping("/booking/remove")
    public void removeBookingById(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (id == -1)
            throw new InvalidIdException();
        this.bookingService.removeBookingById(id);
    }

    @PutMapping("booking/outstanding")
    public void makeOutstandingRequest(@RequestParam(value = "id", defaultValue = "-1") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();

        if (documentId == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(documentId);
        if (document == null) {
            throw new BookingNotFoundException();
        }

        PriorityQueue<Booking> priorityQueue = getQueueForBookById(documentId);

        for (Booking bookItem : priorityQueue) {
            bookItem.setTypeBooking(typeBookingService.findByTypeName("close"));
            bookingService.save(bookItem);

            String message = "Your queue position is cancelled";
            notificationService.newNotification(bookItem.getUser().getId(), message);
        }

        for (Booking bookItem : getHoldersForBookById(documentId))
        {
            bookItem.setTypeBooking(typeBookingService.findByTypeName("outstanding"));
            bookItem.setReturnDate(new Date(System.currentTimeMillis() + DAY_TIME));
            bookingService.save(bookItem);

            String message = "You have to return " + document.getTitle() + " for one day";
            notificationService.newNotification(bookItem.getUser().getId(), message);
        }


        logService.newLog(token.id, "Outstanding request to " + document.getTitle());
    }

    @PutMapping("/booking/renew")
    public void renewBook(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty"))) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Booking booking = bookingService.getBookingById(id);

        if (booking == null) {
            throw new BookingNotFoundException();
        }

        if (!"vp".equals(booking.getUser().getRole().getName()) && "renew".equals(booking.getTypeBooking().getTypeName())) {
            throw new AlreadyRenewException();
        }
        if ("outstanding".equals(booking.getTypeBooking().getTypeName())) {
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

    @GetMapping("/booking/queue")
    public Iterable<Booking> getQueueForBook(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (documentService.findById(id) == null) throw new DocumentNotFoundException();

        return getQueueForBookById(id);
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

            String message = "Your queue position is cancelled";
            notificationService.newNotification(booking.getUser().getId(), message);
            PriorityQueue<Booking> pq = getQueueForBookById(booking.getDocument().getId());
            if (pq.size() > 0) {
                Booking newBooking = pq.peek();
                newBooking.setTypeBooking(typeBookingService.findByTypeName("available"));
                Date returnData = new Date();
                returnData.setTime(System.currentTimeMillis() + AVAILABLE_TIME);
                newBooking.setReturnDate(returnData);
                bookingService.save(newBooking);
                message = "";
                message += booking.getDocument().getTitle();
                message += " is available for you";
                notificationService.newNotification(newBooking.getUser().getId(), message);
            }
            else
            {
                Document document = booking.getDocument();
                document.setCount(document.getCount() + 1);
                documentService.save(document);
            }
        } else {
            getFine(booking);
            String message = "You have fine of ";
            message += Integer.toString(booking.getFine());
            notificationService.newNotification(booking.getUser().getId(), message);
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
            String message = "";
            message += booking.getDocument().getTitle();
            message += " is available for you";
            notificationService.newNotification(booking.getUser().getId(), message);
            document.setCount(document.getCount() - 1);
            documentService.save(document);
        }
    }

    @Scheduled(initialDelay = 0L, fixedDelay = 86400000L)
    public void systemUpdate() {
        Long systemTime = System.currentTimeMillis();
        for (Booking booking : findActiveBookings()) {
            if (booking.getReturnDate().getTime() < systemTime) {
                applyMeasures(booking);
            } else if (booking.getReturnDate().getTime() - systemTime < WEEK_AFTER_END) {
                //TODO: NOTIFICATION ABOUT WEEK AFTER END
            }
        }
    }

    public enum Priority {
        PROFESSOR, VP, TA, INSTRUCTOR, STUDENT
    }

    private class MyComparator implements Comparator<Booking> {
        public int compare(Booking x, Booking y) {
            return convertToEnum(y.getUser().getRole()).compareTo(convertToEnum(x.getUser().getRole()));
        }
    }

    private Priority convertToEnum(Role role) {
        switch (role.getPosition().toLowerCase()) {
            case "student":
                return Priority.STUDENT;
            case "instructor":
                return Priority.INSTRUCTOR;
            case "ta":
                return Priority.TA;
            case "professor":
                return Priority.PROFESSOR;
            case "vp":
                return Priority.VP;
        }
        throw new RoleNotFoundException();
    }

    private List<Booking> getHoldersForBookById(Integer bookId){
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> ("taken".equals(booking.getTypeBooking().getTypeName())
                        || "renew".equals(booking.getTypeBooking().getTypeName())
                        || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    private PriorityQueue<Booking> getQueueForBookById(Integer bookId) {
        PriorityQueue<Booking> queue = new PriorityQueue<>(new MyComparator());

        queue.addAll(bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> ("open".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList()));

        return queue;
    }
}

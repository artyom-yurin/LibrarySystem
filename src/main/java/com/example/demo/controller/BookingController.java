package com.example.demo.controller;

import com.example.demo.common.Privileges;
import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.service.*;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
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

    /***
     * Constructor for Booking Controller. Works on startup of the server
     *
     * @param bookingService
     * @param documentService
     * @param userService
     * @param typeBookingService
     * @param notificationService
     * @param logService
     */
    BookingController(BookingService bookingService, DocumentService documentService, UserService userService, TypeBookingService typeBookingService, NotificationService notificationService, LogService logService) {
        this.bookingService = bookingService;
        this.documentService = documentService;
        this.userService = userService;
        this.typeBookingService = typeBookingService;
        this.notificationService = notificationService;
        this.logService = logService;
    }

    /**
     * Method for finding bookings by the ID of the desired user.
     * @param id    ID of the user
     * @param request   HTTP Servlet Request with a token of the session
     * @return List of bookings made by a specific user
     */
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

    /**
     * Method for returning bookings of the user on request.
     * @param request   HTTP Servlet Request with a token of the session - ID of the user is taken from here
     * @return List of bookings made by a requesting user
     */
    @GetMapping("/booking/findself")
    public Iterable<Booking> findMyBooking(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(token.id))
                .filter(booking -> !("close".equals(booking.getTypeBooking().getTypeName()) || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    /**
     * Method for finding bookings which have been requested for return
     * @param request   HTTP Servlet Request with a token of the session
     * @return List of bookings where users want to return books
     */
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

    /**
     * Method for displaying all bookings currently in the system
     * @param request   HTTP Servlet Request with a token of the session
     * @return List of all bookings in the system
     */
    @GetMapping("/booking/findall")
    public Iterable<Booking> findAllBookings(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        return bookingService.findAll();
    }

    /**
     * Method for displaying all bookings marked as 'available' (user has to come and take the book)
     * @param request   HTTP Servlet Request with a token of the session
     * @return List of all bookings marked as 'available'
     */
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

    /**
     * Method for requesting a document given its ID
     * @param documentId    ID of the desired document
     * @param request   HTTP Servlet Request with a token of the session - current user's ID is taken from here
     */
    @PostMapping("/booking/request")
    public void requestDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
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
            }
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

    /**
     * Method for checking out a book (taking it from the library)
     * @param bookingId ID of the booking of the user which takes a book
     * @param request   HTTP Servlet Request with a token of the session
     */
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

    /**
     * Return a document by the ID of its booking
     * @param id    ID of the booking
     * @param request HTTP Servlet Request with a token of the session - user's ID is taken from here
     */
    @PutMapping("/booking/return")
    public void returnDocumentById(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();
        if (id == -1)
            throw new InvalidIdException();
        Booking booking = bookingService.getBookingById(id);
        if (booking == null)
            throw new BookingNotFoundException();
        booking.setTypeBooking(typeBookingService.findByTypeName("return request"));
        bookingService.save(booking);

        logService.newLog(token.id, "Want return " + booking.getDocument().getTitle());
    }

    /**
     * Method for closing the booking (book was taken from the library and then returned)
     * @param id    ID of the booking
     * @param request HTTP Servlet Request with a token of the session
     */
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

    /**
     * Method from deleting the booking from the system by its ID
     * @param id    ID of the booking
     * @param request   HTTP Servlet Request with a token of the session
     */
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

    /**
     * Method for making an outstanding request on a document
     * @param documentId    ID of the desired document
     * @param request   HTTP Servlet Request with a token of the session
     */
    @PutMapping("booking/outstanding")
    public void makeOutstandingRequest(@RequestParam(value = "id", defaultValue = "-1") Integer documentId, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();

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

    /**
     * Method for renewing the document by ID of the corresponding booking
     * @param id    ID of the booking
     * @param request   HTTP Servlet Request with a token of the session
     */
    @PutMapping("/booking/renew")
    public void renewBook(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!(token.role.equals("patron") || token.role.equals("faculty") || token.role.equals("vp"))) throw new AccessDeniedException();

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

        logService.newLog(token.id, "User " + booking.getUser().getUsername() + " renewed " + booking.getDocument().getTitle());
    }

    /**
     * Method for displaying the priority queue for the document
     * @param id    ID of the document
     * @param request   HTTP Servlet Request with a token of the session
     * @return List of bookings in the correct order
     */
    @GetMapping("/booking/queue")
    public Iterable<Booking> getQueueForBook(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
            throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (documentService.findById(id) == null) throw new DocumentNotFoundException();

        return getQueueForBookById(id);
    }

    /**
     * Method for returning the list of all active bookings (not closed or new bookings)
     * @return List of all active bookings
     */
    public List<Booking> findActiveBookings() {
        return bookingService.findAll()
                .stream()
                .filter(booking -> ("available".equals(booking.getTypeBooking().getTypeName())
                        || "taken".equals(booking.getTypeBooking().getTypeName())
                        || "renew".equals(booking.getTypeBooking().getTypeName())
                        || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    /**
     * Method for applying corresponding measures (send out a notification about smth) to a user
     * @param booking ID of the booking
     */
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

    /**
     * Internal method for calculating fine
     * @param booking Booking with the fine
     */
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

    /**
     * Internal method for creating the queue for the book
     * @param bookID ID of the book
     */
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

    /**
     * Internal method for updating the system
     */
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

    /**
     * Internal method for returning priority of the user
     * @param role user's role (Student, Professor, Visiting Professor, etc)
     * @return Priority of the user
     */
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

    /**
     * Internal method for displaying users who currently have a specified book
     * @param bookId ID of the book
     * @return List of holders of the book
     */
    private List<Booking> getHoldersForBookById(Integer bookId){
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> ("taken".equals(booking.getTypeBooking().getTypeName())
                        || "renew".equals(booking.getTypeBooking().getTypeName())
                        || "return request".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList());
    }

    /**
     * Internal method for constructing a queue for a specified book
     * @param bookId ID of the book
     * @return Queue of users for that book
     */
    private PriorityQueue<Booking> getQueueForBookById(Integer bookId) {
        PriorityQueue<Booking> queue = new PriorityQueue<>(new MyComparator());

        queue.addAll(bookingService.findAll()
                .stream()
                .filter(booking -> booking.getDocument().getId().equals(bookId))
                .filter(booking -> ("open".equals(booking.getTypeBooking().getTypeName())))
                .collect(Collectors.toList()));

        return queue;
    }

    /**
     * Priorities of the user types
     */
    public enum Priority {
        PROFESSOR, VP, TA, INSTRUCTOR, STUDENT
    }

    /**
     * Internal comparator for converting users to simple enumerator
     */
    private class MyComparator implements Comparator<Booking> {
        public int compare(Booking x, Booking y) {
            return convertToEnum(y.getUser().getRole()).compareTo(convertToEnum(x.getUser().getRole()));
        }
    }
}

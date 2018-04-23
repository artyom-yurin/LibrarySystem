package com.example.demo.controller;

import com.example.demo.common.Privileges;
import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.LoginModel;
import com.example.demo.model.UserModel;
import com.example.demo.service.LogService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private RoleService roleService;
    private LogService logService;

    public UserController(UserService userService, RoleService roleService, LogService logService) {
        this.userService = userService;
        this.roleService = roleService;
        this.logService = logService;
    }

    /**
     * Method for loggin in
     * @param loginModel Model of the login (internal representation)
     * @param response HTTP Servlet Response
     * @return Correct view of the website
     */
    @PostMapping("/user/login")
    public ModelAndView login(@RequestBody LoginModel loginModel, HttpServletResponse response) {
        User loginUser = userService.findByUsername(loginModel.getUsername());
        if (loginUser == null) throw new UserNotFoundException();
        if (loginUser.getPassword().equals(loginModel.getPassword())) {
            TokenAuthenticationService.addAuthentication(response, loginUser);
            if (loginUser.getRole().getName().equals("librarian")) {
                return new ModelAndView("books");
            } else {
                return new ModelAndView("catalog");
            }
        }
        throw new PasswordInvalidException();
    }

    /**
     * Method for adding a new user to the system
     * @param userModel Model of the user (internal representation)
     * @param request HTTP Servlet Request with a token of the session
     */
    @PostMapping("/user/add")
    public void addUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) {
            if (!token.role.equals("librarian")) throw new AccessDeniedException();
            if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();
        }
        User user = userService.findByUsername(userModel.getUsername());
        if (user != null) {
            throw new AlreadyUserExistException();
        }
        Role role = roleService.findByPosition(userModel.getPosition().toLowerCase());
        if (role == null) throw new RoleNotFoundException();
        user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhone(), role, userModel.getUsername(), userModel.getPassword());
        userService.save(user);
        logService.newLog(token.id, "Added new user " + user.getUsername());
    }

    /**
     * Method for updating the user information
     * @param userModel Model of the user (internal representation)
     * @param request   HTTP Servlet Request with a token of the session
     */
    @PutMapping("/user/update")
    public void updateUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) {
            if (!token.role.equals("librarian")) throw new AccessDeniedException();
            if (Privileges.Privilege.Priv1.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();
        }

        if (userModel.getId() == null) {
            throw new InvalidIdException();
        }
        User user = userService.findById(userModel.getId());
        if (user == null) {
            throw new UserNotFoundException();
        }
        Role role = roleService.findByPosition(userModel.getPosition().toLowerCase());
        if (role == null) throw new RoleNotFoundException();
        user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhone(), role, userModel.getUsername(), userModel.getPassword());
        user.setId(userModel.getId());
        userService.save(user);

        logService.newLog(token.id, "Updated user by id " + user.getId());
    }

    /**
     * Method for deleting user from the system
     * @param id      ID of the user to delete
     * @param request HTTP Servlet Request with a token of the session
     */
    @Transactional
    @DeleteMapping("/user/remove")
    public void removeUser(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) {
            if (!token.role.equals("librarian")) throw new AccessDeniedException();
            if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();
        }

        if (id == -1) {
            throw new InvalidIdException();
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(id);

        logService.newLog(token.id, "Deleted user " + user.getUsername());
    }

    /**
     * Method for returning all users currently in the system
     * @param request HTTP Servlet Request with a token of the session
     * @return List of all users
     */
    @GetMapping("/user/users")
    public Iterable<User> getUsers(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian"))
            throw new AccessDeniedException();
        return userService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().getName().equals("faculty") || user.getRole().getName().equals("patron") || user.getRole().getName().equals("vp"))
                .collect(Collectors.toList());
    }

    /**
     * Method for displaying all librarians in the system
     * @param request HTTP Servlet Request with a token of the session
     * @return List of all librarians
     */
    @GetMapping("/user/librarians")
    public Iterable<User> getLibrarians(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin"))
            throw new AccessDeniedException();
        return userService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().getName().equals("librarian"))
                .collect(Collectors.toList());
    }
}

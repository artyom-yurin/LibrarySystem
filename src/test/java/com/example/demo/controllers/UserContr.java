package com.example.demo.controllers;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.LoginModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserContr {

    private UserService userService;
    private RoleService roleService;
    private LogService logService;

    public UserContr(UserService userService, RoleService roleService, LogService logService) {
        this.userService = userService;
        this.roleService = roleService;
        this.logService = logService;
    }

    public void addUser(UserModel userModel, Integer librarianId){
        User user = userService.findByUsername(userModel.getUsername());
        if (user != null) {
            throw new AlreadyUserExistException();
        }
        Role role = roleService.findByPosition(userModel.getPosition().toLowerCase());
        if (role == null) throw new RoleNotFoundException();
        user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhone(), role, userModel.getUsername(), userModel.getPassword());
        userService.save(user);
        logService.newLog(librarianId, "Added new user " + user.getUsername());
    }

    public void updateUser(UserModel userModel, Integer libraryId) {
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

        logService.newLog(libraryId, "Updated user by id " + user.getId());
    }

    @Transactional
    public void removeUser(Integer id, Integer libraryId) {
        if (id == -1) {
            throw new InvalidIdException();
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(id);

        logService.newLog(libraryId, "Deleted user " + user.getUsername());
    }

    public Iterable<User> getUsers() {
        return userService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().getName().equals("faculty") || user.getRole().getName().equals("patron") || user.getRole().getName().equals("vp"))
                .collect(Collectors.toList());
    }

    /**
     * Method for displaying all librarians in the system
     * @return List of all librarians
     */
    public List<User> getLibrarians() {
        return userService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().getName().equals("librarian"))
                .collect(Collectors.toList());
    }
}

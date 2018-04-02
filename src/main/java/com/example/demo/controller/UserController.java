package com.example.demo.controller;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.LoginModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    private UserService userService;
    private RoleService roleService;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        userService = new UserService(userRepository);
        roleService = new RoleService(roleRepository);
    }

    @PostMapping("/user/login")
    public ModelAndView login(@RequestBody LoginModel loginModel, HttpServletResponse response) {
        User loginUser = userService.findByUsername(loginModel.getUsername());
        if (loginUser == null) throw new UserNotFoundException();
        if (loginUser.getPassword().equals(loginModel.getPassword())) {
            TokenAuthenticationService.addAuthentication(response, loginUser);
            if (loginUser.getRole().getName().equals("admin")) {
                return new ModelAndView("books");
            } else {
                return new ModelAndView("catalog");
            }
        }
        throw new PasswordInvalidException();
    }

    @PostMapping("/user/add")
    public void addUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();

        if (token.role.equals("admin")) {
            User user = userService.findByUsername(userModel.getUsername());
            if (user != null) {
                throw new AlreadyUserExistException();
            }
            Role role = roleService.findByPosition(userModel.getPosition().toLowerCase());
            if (role == null) throw new RoleNotFoundException();
            user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhone(), role, userModel.getUsername(), userModel.getPassword());
            userService.save(user);
            return;
        }
        throw new AccessDeniedException();
    }

    @PutMapping("/user/update")
    public void updateUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (token.role.equals("admin")) {
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
            return;
        }
        throw new AccessDeniedException();
    }

    @Transactional
    @DeleteMapping("/user/remove")
    public void removeUser(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin"))
            throw new AccessDeniedException();
        if (id == -1) {
            throw new InvalidIdException();
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(id);
    }

    @GetMapping("/user/users")
    public Iterable<User> getUsers(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (token.role.equals("admin")) {
            return userService.getAllUsers();
        }
        throw new AccessDeniedException();
    }
}

package com.example.demo.controllers;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserContr {

    private UserService userService;
    private RoleService roleService;

    public UserContr(UserRepository userRepository, RoleRepository roleRepository) {
        userService = new UserService(userRepository);
        roleService = new RoleService(roleRepository);
    }

    public void addUser(UserModel userModel){
            User user = userService.findByUsername(userModel.getUsername());
            if (user != null) {
                throw new AlreadyUserExistException();
            }
            Role role = roleService.findByPosition(userModel.getPosition().toLowerCase());
            if (role == null) throw new RoleNotFoundException();
            user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhone(), role, userModel.getUsername(), userModel.getPassword());
            userService.save(user);
    }

    public void updateUser(UserModel userModel) {
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
    }

    @Transactional
    public void removeUser(Integer id) {
        if (id == -1) {
            throw new InvalidIdException();
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(id);
    }

    public List<User> getUsers() {
        throw new AccessDeniedException();
    }
}

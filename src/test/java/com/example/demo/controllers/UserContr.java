package com.example.demo.controllers;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.UserModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserContr {

    private UserService userService;
    private RoleService roleService;

    public UserContr(UserRepository userRepository, RoleRepository roleRepository) {
        userService = new UserService(userRepository);
        roleService = new RoleService(roleRepository);
    }

    public void addUserTest(UserModel userModel) {
        User user = userService.findByUsername(userModel.getUsername());
        if (user != null) {
            throw new AlreadyUserExistException();
        }
        Role role = roleService.findByPosition(userModel.getPosition());
        if (role == null) throw new RoleNotFoundException();
        user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhoneNumber(), role, userModel.getUsername(), userModel.getPassword());
        user.setId(-1);
        userService.save(user);
    }

    public void updateUserTest(UserModel userModel) {
        if (userModel.getId() == null) {
            throw new InvalidIdException();
        }
        User user = userService.findById(userModel.getId());
        if (user == null) {
            throw new UserNotFoundException();
        }
        Role role = roleService.findByPosition(userModel.getPosition());
        if (role == null) throw new RoleNotFoundException();
        user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhoneNumber(), role, userModel.getUsername(), userModel.getPassword());
        user.setId(userModel.getId());
        userService.save(user);
    }

    public void removeUserTest(UserModel userModel) {
        if (userModel.getId() == null) {
            throw new InvalidIdException();
        }
        User user = userService.findById(userModel.getId());
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(user.getId());
    }


    @Transactional
    public void removeUserTest(Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        userService.remove(user.getId());
    }

    public Iterable<User> getUsersTest() {
        return userService.getAllUsers();
    }

    public int getAmountTest(){
        int count = 0;
        for(User user : getUsersTest()){
            count++;
        }
        return count;
    }
}

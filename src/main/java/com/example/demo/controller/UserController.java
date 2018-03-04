package com.example.demo.controller;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import com.example.demo.exception.PasswordInvalidException;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.LoginModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    private UserService userService;
    private RoleService roleService;

    public UserController(UserRepository userRepository, RoleRepository roleRepository){
        userService = new UserService(userRepository);
        roleService = new RoleService(roleRepository);
    }

    @PostMapping("/user/login")
    public ModelAndView login(@RequestBody LoginModel loginModel)
    {
        User loginUser = userService.findByUsername(loginModel.getUsername());
        if (loginUser == null) throw new UserNotFoundException();
        if (loginUser.getPassword().equals(loginModel.getPassword()))
        {
            return new ModelAndView("catalog");
        }
        throw new PasswordInvalidException();
    }

    @PostMapping("/user/add")
    public void addUser(@RequestBody UserModel userModel)
    {
        int id;
        try
        {
            id = roleService.getIdByName(userModel.getRole());
        }
        catch (Exception e)
        {
            throw e;
        }
        Role role = roleService.findById(id);
        User user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhoneNumber(), role, userModel.getUsername(), userModel.getPassword());
        userService.save(user);
    }

    @PutMapping("/user/update")
    public void updateUser(@RequestBody UserModel userModel)
    {
        int id;
        try {
            id = roleService.getIdByName(userModel.getRole());
        }
        catch (Exception e)
        {
            throw e;
        }
        Role role = roleService.findById(id);
        User user = new User(userModel.getName(), userModel.getSurname(), userModel.getAddress(), userModel.getPhoneNumber(), role, userModel.getUsername(), userModel.getPassword());
        user.setId(userModel.getId());
        userService.save(user);
    }

    @Transactional
    @DeleteMapping("/user/remove")
    public void removeUser(@RequestBody User user)
    {
        userService.remove(user.getId());
    }

    @GetMapping("/user/find")
    public  User getUser(@RequestParam(value = "username", defaultValue = "") String username)
    {
        User findUser = userService.findByUsername(username);
        if (findUser == null) throw new UserNotFoundException();
        return findUser;
    }

    @GetMapping("/user/users")
    public Iterable<User> getUsers()
    {
        return userService.getAllUsers();
    }
}

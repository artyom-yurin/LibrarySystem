package com.example.demo.controller;

import com.example.demo.model.LoginModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ApiController {

    @PostMapping("/api/login")
    public ModelAndView login(@RequestBody LoginModel loginModel)
    {
        if ("login".equals(loginModel.getUsername()) && "password".equals(loginModel.getPassword()))
        {
            return new ModelAndView("catalog");
        }
        return null;
    }
}

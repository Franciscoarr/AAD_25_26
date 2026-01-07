package com.farrnav3006.aad.controller;

import com.farrnav3006.aad.model.User;
import com.farrnav3006.aad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/saveUser",method= RequestMethod.POST)
    public Boolean saveUser(@RequestBody User u){
        return userService.saveUser(u);
    }
}

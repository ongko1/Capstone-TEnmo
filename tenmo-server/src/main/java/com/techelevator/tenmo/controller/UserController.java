package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path="/users/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int id) {
        return userDao.getUserByUserId(id);
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userDao.findAll();
    }
}

package SecureKeeper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.UsersModel;
import SecureKeeper.service.UserService;

// Endpoints for Login and Register
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public UsersModel register(@RequestBody UsersModel user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UsersModel user) {
        return service.verify(user);
    }
}
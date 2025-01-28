package SecureKeeper.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.UsersModel;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    
    private List<UsersModel> users = new ArrayList<>(List.of(
        new UsersModel(0l, "linda", "123"),
        new UsersModel(1l, "linda", "123")
    ));

    @GetMapping("/users")
    public List<UsersModel> getStudents() {
        return users;
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/users")
    public UsersModel addUser(@RequestBody UsersModel user) {
        users.add(user);
        return  user;
    }
}
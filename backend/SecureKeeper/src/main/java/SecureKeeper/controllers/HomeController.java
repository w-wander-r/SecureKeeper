package SecureKeeper.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    // TODO send a list of notes(note:user:email:password ...etc)
    @GetMapping
    public String home(HttpServletRequest request) {
        return  ""+request.getSession().getId();
    }
}
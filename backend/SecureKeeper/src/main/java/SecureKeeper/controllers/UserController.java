package SecureKeeper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SecureKeeper.models.UserDTO;
import SecureKeeper.models.UsersModel;
import SecureKeeper.service.UserService;

// TODO: fix id requesting in json
// TODO: doc for UserController, UserService, UserDTO, UserPrincipal
// Endpoints for Login and Register
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        UsersModel user = new UsersModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        UsersModel savedUser = service.register(user);

        UserDTO savedUserDTO = new UserDTO();
        savedUserDTO.setUsername(savedUser.getUsername());
        savedUserDTO.setPassword(savedUser.getPassword());

        return savedUserDTO;
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        UsersModel user = new UsersModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        return service.verify(user);
    }
}
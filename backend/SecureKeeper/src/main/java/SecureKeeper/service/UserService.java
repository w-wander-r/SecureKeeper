package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo repo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UsersModel register(UsersModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    // verify the user for login page
    public String verify(UsersModel user) {
        Authentication authentication =
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // todo
        return authentication.isAuthenticated() ? jwtService.generateToken() : "fail";
        // return null;
    }
}

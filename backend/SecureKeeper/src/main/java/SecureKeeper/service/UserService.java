package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.UserRepo;

@Service
public class UserService {
    
    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UsersModel register(UsersModel user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
}

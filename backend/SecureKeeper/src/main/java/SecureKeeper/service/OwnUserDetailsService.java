package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SecureKeeper.models.UserPrincipal;
import SecureKeeper.models.User;
import SecureKeeper.repo.UserRepo;

@Service
public class OwnUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = repo.findByUsername(username).orElse(null);

        if(user == null) throw new UsernameNotFoundException("User not found");
        
        return new UserPrincipal(user);
    }
    
}

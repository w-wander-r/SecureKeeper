package SecureKeeper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SecureKeeper.models.UserPrincipal;
import SecureKeeper.models.UsersModel;
import SecureKeeper.repo.UserRepo;

@Service
public class OwnUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        UsersModel user = repo.findByUsername(username);

        if(user == null) throw new UsernameNotFoundException("User not found");
        
        return new UserPrincipal(user);
    }
    
}

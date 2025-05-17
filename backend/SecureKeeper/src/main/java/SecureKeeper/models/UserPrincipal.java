package SecureKeeper.models;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom implementation of Spring Security's {@link UserDetails} that adapts the
 * application's {@link UsersModel} to Spring Security's authentication framework.
 * 
 * <p>This class serves as the bridge between the application's user representation
 * and Spring Security's requirements for user authentication and authorization.</p>
 * 
 * <p><strong>Key Responsibilities:</strong>
 * <ul>
 *   <li>Wraps a {@link UsersModel} entity to make it compatible with Spring Security</li>
 *   <li>Provides user credentials (username and password) to the security framework</li>
 *   <li>Defines the user's granted authorities (roles/permissions)</li>
 * </ul>
 * </p>
 * 
 * @see UserDetails
 * @see UsersModel
 */
public class UserPrincipal implements UserDetails{

    private UsersModel user;

    public UserPrincipal(UsersModel user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user.
     * 
     * <p>Current implementation grants all users a single "USER" authority.
     * For role-based access control, modify this to return authorities based
     * on the user's actual roles stored in {@link UsersModel}.</p>
     * 
     * @return an immutable collection containing a single "USER" authority
     * @see Collections#singleton(Object)
     * @see SimpleGrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}

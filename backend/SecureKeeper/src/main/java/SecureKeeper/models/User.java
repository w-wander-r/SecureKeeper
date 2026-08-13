package SecureKeeper.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Builder(builderMethodName = "internalBuilder")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Folder> folders;

    @Override
    public String toString() {
        return "UsersModel [id=" + id + ", username=" + username + "]";
    }

    // Controlled creation ------
    public static User createNewUser(String username, String rawPassword) {
        User user = new User();
        user.username = username;
        user.password = rawPassword;
        return user;
    }

    public static User createForAuthentication(String username, String rawPassword) {
        User user = new User();
        user.username = username;
        user.password = rawPassword;
        return user;
    }

    public void updateHashedPassword(String hashedPassword) {
        this.password = hashedPassword;
    }
}

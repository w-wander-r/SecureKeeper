package SecureKeeper.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    // list?
    Optional<User> findByUsername(String username);
}

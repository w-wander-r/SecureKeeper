package SecureKeeper.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.UsersModel;

@Repository
public interface UserRepo extends JpaRepository<UsersModel, Long>{
    Optional<UsersModel> findByUsername(String username);
}

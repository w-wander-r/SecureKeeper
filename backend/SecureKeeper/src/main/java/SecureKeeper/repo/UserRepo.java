package SecureKeeper.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SecureKeeper.models.UsersModel;

@Repository
public interface UserRepo extends JpaRepository<UsersModel, Long>{
    UsersModel findByUsername(String username);
}

package SecureKeeper.repo;

import SecureKeeper.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL"
})
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void UserRepo_SaveAll_ReturnSavedUser() {

        // Arrange
        User user = User.builder()
                .username("wander")
                .password("www")
                .build();

        // Act
        User savedUser = userRepo.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_GetAll_ReturnMoreThenOneUser() {
        User user1 = User.builder()
                .username("first_user")
                .password("123")
                .build();

        User user2 = User.builder()
                .username("second_user")
                .password("123")
                .build();

        userRepo.save(user1);
        userRepo.save(user2);

        List<User> users = userRepo.findAll();

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserRepo_FindById_ReturnSavedUser()
    {
        User user1 = User.builder()
                .username("first_user")
                .password("123")
                .build();

        userRepo.save(user1);

        User userList = userRepo.findById(user1.getId()).get();

        Assertions.assertThat(userList).isNotNull();
    }

    @Test
    public void UserRepo_FindByUsername_ReturnUserNotNull() {
        User user1 = User.builder()
                .username("wander")
                .password("wander")
                .build();

        userRepo.save(user1);

        User userList = userRepo.findByUsername(user1.getUsername()).get();

        Assertions.assertThat(userList).isNotNull();
    }
}

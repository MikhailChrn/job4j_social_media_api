package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<User> userOptional = userRepository.findById(1);

        assertThat(userOptional).isEmpty();
    }

    @Test
    public void whenSavePersonThenFindById() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("pass");
        userRepository.save(user);
        Optional<User> foundedUser = userRepository.findById(user.getId());
        assertThat(foundedUser).isPresent();
        assertThat(foundedUser.get().getEmail())
                .isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        User user1 = User.builder().email("email 1").password("pass")
                .build();
        User user2 = User.builder().email("email 2").password("pass")
                .build();
        User user3 = User.builder().email("email 3").password("pass")
                .build();

        List.of(user3, user2, user1)
                .forEach(user -> userRepository.save(user));

        Collection<User> expected = List.of(user1, user2, user3);

        Collection<User> userRepositoryResponse = (Collection<User>) userRepository.findAll();

        assertEquals(userRepositoryResponse.size(), expected.size());
        assertTrue(userRepositoryResponse.containsAll(expected));
    }
}

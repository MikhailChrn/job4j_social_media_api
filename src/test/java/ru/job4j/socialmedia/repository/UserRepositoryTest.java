package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Friendship;
import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @BeforeEach
    public void deleteAllBefore() {
        subscribeRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAllAfterAll() {
        subscribeRepository.deleteAll();
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<User> userOptional = userRepository.findById(1);

        assertThat(userOptional).isEmpty();
    }

    @Test
    public void whenSaveUserThenFindById() {
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

    @Test
    void whenSaveUserThenFindByEmailAndPassword() {
        User userOne = User.builder().email("One@Email").password("pass")
                .build();
        User userTwo = User.builder().email("Two@Email").password("pass")
                .build();
        User userThree = User.builder().email("Three@Email").password("pass")
                .build();
        List.of(userThree, userOne, userTwo)
                .forEach(user -> userRepository.save(user));

        assertEquals(userRepository.findAll().size(), 3);
        assertEquals(userRepository.findByEmailAndPassword(userTwo.getEmail(),
                        userTwo.getPassword()).get(), userTwo);
    }

    @Test
    void whenFindAllSubscribersThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User userOne = User.builder().email("One@Email").password("pass")
                .build();
        User userTwo = User.builder().email("Two@Email").password("pass")
                .build();
        User userThree = User.builder().email("Three@Email").password("pass")
                .build();
        User userFour = User.builder().email("Four@Email").password("pass")
                .build();
        User userFive = User.builder().email("Five@Email").password("pass")
                .build();
        List.of(userThree, userFive, userOne, userTwo, userFour)
                .forEach(user -> userRepository.save(user));
        Subscribe subscribe1 = Subscribe.builder().userFrom(userOne)
                .subscribeUserTo(userThree).created(now).build();
        Subscribe subscribe2 = Subscribe.builder().userFrom(userTwo)
                .subscribeUserTo(userThree).created(now).build();
        Subscribe subscribe3 = Subscribe.builder().userFrom(userThree)
                .subscribeUserTo(userFive).created(now).build();
        Subscribe subscribe4 = Subscribe.builder().userFrom(userFour)
                .subscribeUserTo(userFive).created(now).build();
        List.of(subscribe2, subscribe1, subscribe4, subscribe3)
                .forEach(subscribe -> subscribeRepository.save(subscribe));

        Collection<User> listOfUserThree = List.of(subscribe2, subscribe1).stream()
                .map(Subscribe::getUserFrom).toList();
        Collection<User> listOfUserFive = List.of(subscribe3, subscribe4).stream()
                .map(Subscribe::getUserFrom).toList();

        Collection<User> userRepositoryResponseThree =
                userRepository.findAllSubscribersByUserId(userThree.getId());
        Collection<User> userRepositoryResponseFive =
                userRepository.findAllSubscribersByUserId(userFive.getId());
        assertEquals(userRepositoryResponseThree.size(), listOfUserThree.size());
        assertTrue(userRepositoryResponseThree.containsAll(listOfUserThree));
        assertEquals(userRepositoryResponseFive.size(), listOfUserFive.size());
        assertTrue(userRepositoryResponseFive.containsAll(listOfUserFive));
    }

    @Test
    void whenFindAllFriendsThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User userOne = User.builder().email("One@Email").password("pass")
                .build();
        User userTwo = User.builder().email("Two@Email").password("pass")
                .build();
        User userThree = User.builder().email("Three@Email").password("pass")
                .build();
        User userFour = User.builder().email("Four@Email").password("pass")
                .build();
        User userFive = User.builder().email("Five@Email").password("pass")
                .build();
        List.of(userThree, userFive, userOne, userTwo, userFour)
                .forEach(user -> userRepository.save(user));
        Friendship friendship1 = Friendship.builder().userFrom(userThree)
                .userTo(userOne).created(now).build();
        Friendship friendship2 = Friendship.builder().userFrom(userThree)
                .userTo(userTwo).created(now).build();
        Friendship friendship3 = Friendship.builder().userFrom(userFive)
                .userTo(userThree).created(now).build();
        Friendship friendship4 = Friendship.builder().userFrom(userFive)
                .userTo(userFour).created(now).build();
        List.of(friendship2, friendship1, friendship4, friendship3)
                .forEach(friendship -> friendshipRepository.save(friendship));

        Collection<User> listOfUserThree = List.of(friendship2, friendship1).stream()
                .map(Friendship::getUserTo).toList();
        Collection<User> listOfUserFive = List.of(friendship3, friendship4).stream()
                .map(Friendship::getUserTo).toList();
        Collection<User> userRepositoryResponseThree =
                userRepository.findAllFriendsByUserId(userThree.getId());
        Collection<User> userRepositoryResponseFive =
                userRepository.findAllFriendsByUserId(userFive.getId());
        assertEquals(userRepositoryResponseThree.size(), listOfUserThree.size());
        assertTrue(userRepositoryResponseThree.containsAll(listOfUserThree));
        assertEquals(userRepositoryResponseFive.size(), listOfUserFive.size());
        assertTrue(userRepositoryResponseFive.containsAll(listOfUserFive));
    }

    @Test
    void whenCreateThenUpdateEntity() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User userBefore = User.builder().email("Before@Email")
                .password("pass")
                .create(now).build();
        userRepository.save(userBefore);
        User userUpdated = User.builder().id(userBefore.getId())
                .email("After@Email")
                .password(userBefore.getPassword())
                .create(userBefore.getCreate()).build();
        assertEquals(userRepository.update(userUpdated), 1);
        User userAfter = userRepository.findById(userBefore.getId()).get();
        assertEquals(userUpdated.getEmail(), userAfter.getEmail());
    }

    @Test
    void whenDeleteThenGetSuccess() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User user = User.builder().email("Email@Email")
                .password("pass")
                .create(now).build();
        userRepository.save(user);
        int id = user.getId();
        User userFounded = userRepository.findById(user.getId()).get();
        assertEquals(userFounded, user);
        userRepository.deleteById(id);
        assertTrue(userRepository.findById(id).isEmpty());
    }
}

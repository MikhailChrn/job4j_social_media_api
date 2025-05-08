package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Friendship;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FriendshipRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @BeforeEach
    public void deleteAllPost() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<Friendship> friendshipOptional = friendshipRepository.findById(1);

        assertThat(friendshipOptional).isEmpty();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user1 = User.builder().email("user #1")
                .password("pass").build();
        User user2 = User.builder().email("user #2")
                .password("pass").build();
        User user3 = User.builder().email("user #3")
                .password("pass").build();
        User user4 = User.builder().email("user #4")
                .password("pass").build();
        User user5 = User.builder().email("user #5")
                .password("pass").build();
        List.of(user4, user2, user5, user1, user3)
                .forEach(user -> userRepository.save(user));

        Friendship friendship1 = Friendship.builder().userFrom(user2)
                .userTo(user5).created(now).build();
        Friendship friendship2 = Friendship.builder().userFrom(user4)
                .userTo(user3).created(now).build();
        Friendship friendship3 = Friendship.builder().userFrom(user1)
                .userTo(user2).created(now).build();
        List.of(friendship2, friendship1, friendship3)
                .forEach(friendship -> friendshipRepository.save(friendship));

        Collection<Friendship> friendshipAfterRepository = friendshipRepository.findAll();

        assertEquals(friendshipAfterRepository.size(), 3);
        assertTrue(friendshipAfterRepository
                .containsAll(List.of(friendship2, friendship3, friendship1)));

        assertEquals(friendshipRepository.findById(friendship2.getId()).get().getUserTo(),
                user3);
    }
}
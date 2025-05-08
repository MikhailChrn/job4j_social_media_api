package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Subscribe;
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
class SubscribeRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @BeforeEach
    public void deleteAllPost() {
        subscribeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        subscribeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<Subscribe> subscribeOptional = subscribeRepository.findById(1);

        assertThat(subscribeOptional).isEmpty();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User userFrom = User.builder().email("user from")
                .password("pass").build();
        User subscribeToUser = User.builder().email("user to")
                .password("pass").build();
        List.of(subscribeToUser, userFrom)
                .forEach(user -> userRepository.save(user));

        Subscribe subscribe1 = Subscribe.builder().userFrom(userFrom)
                .subscribeUserTo(subscribeToUser).created(now).build();
        Subscribe subscribe2 = Subscribe.builder().userFrom(subscribeToUser)
                .subscribeUserTo(userFrom).created(now).build();

        List.of(subscribe2, subscribe1)
                .forEach(subscribe -> subscribeRepository.save(subscribe));

        Collection<Subscribe> subscribesAfterRepository = subscribeRepository.findAll();

        assertEquals(subscribesAfterRepository.size(), 2);
        assertTrue(subscribesAfterRepository
                .containsAll(List.of(subscribe1, subscribe2)));

        assertEquals(subscribeRepository.findById(subscribe2.getId()).get().getSubscribeUserTo(),
                userFrom);
    }
}
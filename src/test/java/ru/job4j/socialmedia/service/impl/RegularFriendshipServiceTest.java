package ru.job4j.socialmedia.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.FriendshipService;
import ru.job4j.socialmedia.service.SubscribeService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegularFriendshipServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private FriendshipService friendshipService;

    @BeforeEach
    public void deleteAllPost() {
        friendshipService.deleteAll();
        subscribeService.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        friendshipService.deleteAll();
        subscribeService.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenSaveFriendshipThenGetAllEntitiesThroughServiceThenDeleteThisAndGetNothing() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User userFrom = User.builder().email("user from").password("pass").build();
        User userTo = User.builder().email("user to").password("pass").build();
        List.of(userTo, userFrom).forEach(user -> userRepository.save(user));
        Subscribe subscribe = Subscribe.builder().userFrom(userFrom).subscribeUserTo(userTo).created(now).build();
        List.of(subscribe).forEach(sbscrb -> subscribeService.save(sbscrb));

        Collection<Subscribe> subscribesThroughService = subscribeService.findAll();
        assertEquals(subscribesThroughService.size(), 1);
        assertTrue(subscribesThroughService.containsAll(List.of(subscribe)));
        assertEquals(friendshipService.findAll().size(), 0);
        friendshipService.addFriendshipBetweenUsers(userFrom, userTo);
        subscribesThroughService = subscribeService.findAll();
        assertEquals(subscribesThroughService.size(), 2);
        assertEquals(friendshipService.findAll().size(), 2);

        int subscribeId = subscribeService.findByUserFromAndUserTo(userFrom, userTo).get().getId();
        int frienshipId = friendshipService.findByUserFromAndUserTo(userFrom, userTo).get().getId();
        assertEquals(subscribeService.findById(subscribeId).get().getUserFrom(), userFrom);
        assertEquals(subscribeService.findById(subscribeId).get().getSubscribeUserTo(), userTo);
        assertEquals(subscribeService.findById(subscribeId + 1).get().getUserFrom(), userTo);
        assertEquals(subscribeService.findById(subscribeId + 1).get().getSubscribeUserTo(), userFrom);
        assertEquals(friendshipService.findById(frienshipId).get().getUserFrom(), userFrom);
        assertEquals(friendshipService.findById(frienshipId).get().getUserTo(), userTo);
        assertEquals(friendshipService.findById(frienshipId + 1).get().getUserFrom(), userTo);
        assertEquals(friendshipService.findById(frienshipId + 1).get().getUserTo(), userFrom);
        assertTrue(friendshipService.removeFriendship(friendshipService.findByUserFromAndUserTo(userFrom, userTo).get()));
        assertEquals(friendshipService.findAll().size(), 0);
        assertEquals(subscribeService.findAll().size(), 1);
        assertEquals(subscribeService.findAll().stream()
                .findFirst().get().getUserFrom(), userTo);
    }
}
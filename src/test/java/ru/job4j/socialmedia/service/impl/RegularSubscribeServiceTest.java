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
import ru.job4j.socialmedia.service.SubscribeService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegularSubscribeServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeService subscribeService;

    @BeforeEach
    public void deleteAllPost() {
        subscribeService.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        subscribeService.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntitiesThroughService() {
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
                .forEach(subscribe -> subscribeService.save(subscribe));

        Collection<Subscribe> subscribesThroughService = subscribeService.findAll();

        assertEquals(subscribesThroughService.size(), 2);
        assertTrue(subscribesThroughService
                .containsAll(List.of(subscribe1, subscribe2)));

        assertEquals(subscribeService.findById(subscribe2.getId()).get().getSubscribeUserTo(),
                userFrom);
    }

    @Test
    public void whenSendRequestsThenUserReceivesListOfAllTheirRequestsToHim() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user1 = User.builder().email("user1_mail")
                .password("pass").build();
        User user2 = User.builder().email("user2_mail")
                .password("pass").build();
        User user3 = User.builder().email("user3_mail")
                .password("pass").build();
        User user4 = User.builder().email("user4_mail")
                .password("pass").build();
        User receiver = User.builder().email("receiver_mail")
                .password("pass").build();
        List.of(user2, user4, receiver, user3, user1)
                .forEach(user -> userRepository.save(user));

        Subscribe subscribe1 = Subscribe.builder().userFrom(user1)
                .subscribeUserTo(receiver).created(now).build();
        Subscribe subscribe2 = Subscribe.builder().userFrom(user2)
                .subscribeUserTo(receiver).created(now).build();
        Subscribe subscribe3 = Subscribe.builder().userFrom(user3)
                .subscribeUserTo(user4).created(now).build();
        Subscribe subscribe4 = Subscribe.builder().userFrom(user4)
                .subscribeUserTo(receiver).created(now).build();
        List.of(subscribe2, subscribe4, subscribe1, subscribe3)
                .forEach(subscribe -> subscribeService.save(subscribe));

        Collection<Subscribe> listOfAllRequestsToUser =
                List.of(subscribe2, subscribe4, subscribe1);
        Collection<Subscribe> responseOfSubscribeService =
                subscribeService.findByUserTo(receiver.getId());

        assertThat(responseOfSubscribeService.size())
                .isEqualTo(listOfAllRequestsToUser.size());
        assertThat(responseOfSubscribeService
                .containsAll(listOfAllRequestsToUser));
    }

    @Test
    public void whenUserCleansHisListOfRequestsFromHimThenHeManageToDoThis() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User user1 = User.builder().email("user1_mail")
                .password("pass").build();
        User user2 = User.builder().email("user2_mail")
                .password("pass").build();
        User user3 = User.builder().email("user3_mail")
                .password("pass").build();
        User user4 = User.builder().email("user4_mail")
                .password("pass").build();
        User requester = User.builder().email("requester_mail")
                .password("pass").build();
        List.of(user2, user4, requester, user3, user1)
                .forEach(user -> userRepository.save(user));
        Subscribe subscribe1 = Subscribe.builder().userFrom(requester)
                .subscribeUserTo(user1).created(now).build();
        Subscribe subscribe2 = Subscribe.builder().userFrom(requester)
                .subscribeUserTo(user2).created(now).build();
        Subscribe subscribe3 = Subscribe.builder().userFrom(user3)
                .subscribeUserTo(user4).created(now).build();
        Subscribe subscribe4 = Subscribe.builder().userFrom(requester)
                .subscribeUserTo(user4).created(now).build();
        List.of(subscribe2, subscribe4, subscribe1, subscribe3)
                .forEach(subscribe -> subscribeService.save(subscribe));

        Collection<Subscribe> listOfAllHisRequestsFromUser =
                List.of(subscribe2, subscribe4, subscribe1);
        Collection<Subscribe> responseOfSubscribeService =
                subscribeService.findByUserFrom(requester.getId());
        assertThat(responseOfSubscribeService.size())
                .isEqualTo(listOfAllHisRequestsFromUser.size());
        assertThat(responseOfSubscribeService
                .containsAll(listOfAllHisRequestsFromUser));
        responseOfSubscribeService.forEach(
                subscribe -> subscribeService.deleteById(subscribe.getId()));
        assertTrue(subscribeService.findByUserFrom(requester.getId()).isEmpty());
    }
}
package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Message;
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
class MessageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    public void deleteAllPost() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<Message> messageOptional = messageRepository.findById(1);

        assertThat(messageOptional).isEmpty();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User userFrom = User.builder().email("user from")
                .password("pass").build();
        User userTo = User.builder().email("user to")
                .password("pass").build();
        List.of(userTo, userFrom)
                .forEach(user -> userRepository.save(user));

        Message message1 = Message.builder().userFrom(userFrom)
                .userTo(userTo).content("content #1")
                .created(now).build();
        Message message2 = Message.builder().userFrom(userTo)
                .userTo(userFrom).content("content #2")
                .created(now).build();
        Message message3 = Message.builder().userFrom(userFrom)
                .userTo(userTo).content("content #3")
                .created(now).build();
        List.of(message3, message1, message2)
                .forEach(message -> messageRepository.save(message));

        Collection<Message> messagesAfterRepository =
                (Collection<Message>) messageRepository.findAll();

        assertEquals(messagesAfterRepository.size(), 3);
        assertTrue(messagesAfterRepository
                .containsAll(List.of(message3, message2, message1)));

        assertEquals(messageRepository.findById(message2.getId()).get().getContent(),
                message2.getContent());

        assertEquals(messageRepository.findById(message2.getId()).get().getUserTo(),
                userFrom);
    }
}
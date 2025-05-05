package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.Post;
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
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void deleteAllPost() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<Post> postOptional = postRepository.findById(1);

        assertThat(postOptional).isEmpty();
    }

    @Test
    public void whenSavePostThenFindById() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = User.builder().email("john.doe@example.com")
                .password("pass").build();
        userRepository.save(user);

        Post post = Post.builder().created(now)
                .title("example")
                .user(user).build();
        postRepository.save(post);

        Optional<Post> foundedPost = postRepository.findById(post.getId());

        assertThat(foundedPost).isPresent();
        assertThat(foundedPost.get().getUser().getEmail())
                .isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = User.builder().email("email").password("pass")
                .build();
        userRepository.save(user);

        Post post1 = Post.builder().created(now)
                .title("example1")
                .user(user).build();
        Post post2 = Post.builder().created(now)
                .title("example1")
                .user(user).build();
        Post post3 = Post.builder().created(now)
                .title("example1")
                .user(user).build();
        List.of(post3, post1, post2)
                .forEach(post -> postRepository.save(post));

        Collection<Post> expected = List.of(post1, post2, post3);

        Collection<Post> postRepositoryResponse = (Collection<Post>) postRepository.findAll();

        assertEquals(postRepositoryResponse.size(), expected.size());
        assertTrue(postRepositoryResponse.containsAll(expected));
    }
}
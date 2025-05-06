package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.entity.User;

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

    @Test
    public void whenGetListPostsOfUserThenGetAllEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User userOne = User.builder().email("One@Email").password("pass")
                .build();
        User userTwo = User.builder().email("Two@Email").password("pass")
                .build();
        List.of(userTwo, userOne)
                .forEach(user -> userRepository.save(user));

        Post post1 = Post.builder().created(now)
                .title("example1")
                .user(userTwo).build();
        Post post2 = Post.builder().created(now)
                .title("example1")
                .user(userTwo).build();
        Post post3 = Post.builder().created(now)
                .title("example1")
                .user(userOne).build();
        Post post4 = Post.builder().created(now)
                .title("example1")
                .user(userTwo).build();
        List.of(post3, post1, post4, post2)
                .forEach(post -> postRepository.save(post));

        Collection<Post> listOfUserTwo = List.of(post4, post2, post1);
        Collection<Post> listOfUserOne = List.of(post3);

        Collection<Post> postRepositoryResponseOne = (Collection<Post>)
                postRepository.findByUser(userOne);
        Collection<Post> postRepositoryResponseTwo = (Collection<Post>)
                postRepository.findByUser(userTwo);

        assertEquals(postRepositoryResponseOne.size(), listOfUserOne.size());
        assertTrue(postRepositoryResponseOne.containsAll(listOfUserOne));
        assertEquals(postRepositoryResponseTwo.size(), listOfUserTwo.size());
        assertTrue(postRepositoryResponseTwo.containsAll(listOfUserTwo));
    }

    @Test
    public void whenGetListPostsBetweenTwoDatesThenGetEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        List<LocalDateTime> dates = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            dates.add(LocalDateTime.of(2024, month, 1, 0, 0));
        }

        User user = User.builder().email("One@Email")
                .password("pass")
                .build();
        userRepository.save(user);

        dates.forEach(date -> postRepository.save(
                Post.builder().user(user).title("title")
                        .created(date).build()));

        Collection<Post> postRepositoryResponse = postRepository.findByCreatedGreaterThanEqualAndCreatedLessThanEqual
                        (LocalDateTime.of(2024, 2, 15, 0, 0),
                                LocalDateTime.of(2024, 8, 15, 0, 0));

        assertEquals(postRepositoryResponse.size(), 6);
    }

    @Test
    public void whenGetListPostsUsingPageThenGetPageWithEntities() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        List<LocalDateTime> dates = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            dates.add(LocalDateTime.of(2024, month, 1, 0, 0));
        }

        User user = User.builder().email("One@Email")
                .password("pass")
                .build();
        userRepository.save(user);

        dates.forEach(date -> postRepository.save(
                Post.builder().user(user).title("title")
                        .created(date).build()));

        Page<Post> postRepositoryResponse =
                postRepository.findByOrderByCreatedDesc(PageRequest.of(2, 2));

        assertEquals(postRepositoryResponse.getSize(), 2);

        assertThat(postRepositoryResponse.stream().findFirst().get().getCreated())
                .isEqualTo(LocalDateTime.of(2024, 8, 1, 0, 0));
    }
}
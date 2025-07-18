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

        Collection<Post> postRepositoryResponse = postRepository.findAll();

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

        Collection<Post> postRepositoryResponseOne = postRepository.findByUser(userOne);
        Collection<Post> postRepositoryResponseTwo = postRepository.findByUser(userTwo);

        assertEquals(postRepositoryResponseOne.size(), listOfUserOne.size());
        assertTrue(postRepositoryResponseOne.containsAll(listOfUserOne));
        assertEquals(postRepositoryResponseTwo.size(), listOfUserTwo.size());
        assertTrue(postRepositoryResponseTwo.containsAll(listOfUserTwo));
    }

    @Test
    public void whenGetListPostsBetweenTwoDatesThenGetEntities() {
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

        Collection<Post> postRepositoryResponse = postRepository.findByCreatedGreaterThanEqualAndCreatedLessThanEqual(
                LocalDateTime.of(2024, 2, 15, 0, 0),
                LocalDateTime.of(2024, 8, 15, 0, 0));

        assertEquals(postRepositoryResponse.size(), 6);
    }

    @Test
    public void whenGetListPostsUsingPageThenGetPageWithEntities() {
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
                postRepository.findALLOrderByCreatedDesc(PageRequest.of(2, 2));

        assertEquals(postRepositoryResponse.getSize(), 2);

        assertThat(postRepositoryResponse.stream().findFirst().get().getCreated())
                .isEqualTo(LocalDateTime.of(2024, 8, 1, 0, 0));
    }

    @Test
    public void whenUpdatePostTitleAndContentByIdThenGetSuccess() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = User.builder().email("One@user")
                .password("pass").build();
        userRepository.save(user);

        Post postBefore = Post.builder().created(now)
                .title("title before")
                .content("content before")
                .user(user).build();
        postRepository.save(postBefore);

        Post postAfter = Post.builder()
                .id(postBefore.getId())
                .created(postBefore.getCreated())
                .title("title after")
                .content("content after")
                .user(postBefore.getUser()).build();
        int count = postRepository.updateTitleAndContentById(postAfter.getId(),
                postAfter.getTitle(),
                postAfter.getContent());

        assertEquals(count, 1);
        assertEquals(postRepository.findById(postBefore.getId()).get(), postAfter);
        assertEquals(postRepository.findById(postBefore.getId()).get().getContent(),
                postAfter.getContent());
    }

    @Test
    public void whenDeletePostByIdThenGetSeccussResult() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user = User.builder().email("One@user")
                .password("pass").build();
        userRepository.save(user);

        Post post = Post.builder().created(now)
                .title("title")
                .content("content")
                .user(user).build();
        postRepository.save(post);

        assertEquals(postRepository.findById(post.getId()).get().getTitle(), "title");

        int count = postRepository.deleteById(post.getId());

        assertEquals(count, 1);
        assertTrue(postRepository.findById(post.getId()).isEmpty());
    }

    @Test
    public void whenFindPostByListOfUsers() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        User user1 = User.builder().email("One@user").password("pass").build();
        User user2 = User.builder().email("Two@user").password("pass").build();
        User user3 = User.builder().email("Three@user").password("pass").build();
        List.of(user3, user2, user1).forEach(userRepository::save);

        Post post1 = Post.builder().created(now).title("example1").user(user3).build();
        Post post2 = Post.builder().created(now).title("example2").user(user3).build();
        Post post3 = Post.builder().created(now).title("example3").user(user1).build();
        Post post4 = Post.builder().created(now).title("example4").user(user3).build();
        List.of(post3, post1, post4, post2).forEach(postRepository::save);

        assertEquals(postRepository.findByUserIdIn(List.of(user2.getId())).size(), 0);

        assertEquals(postRepository.findByUserIdIn(List.of(user1.getId())).size(), 1);
        assertTrue(postRepository.findByUserIdIn(List.of(user1.getId())).containsAll(List.of(post3)));

        assertEquals(postRepository.findByUserIdIn(List.of(user3.getId())).size(), 3);
        assertTrue(postRepository.findByUserIdIn(List.of(user3.getId()))
                .containsAll(List.of(post4, post2, post1)));
    }
}
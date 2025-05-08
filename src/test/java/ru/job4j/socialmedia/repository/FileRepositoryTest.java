package ru.job4j.socialmedia.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.socialmedia.entity.File;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    public void deleteAllPost() {
        fileRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAll() {
        fileRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<File> fileOptional = fileRepository.findById(1);

        assertThat(fileOptional).isEmpty();
    }

    @Test
    public void whenSaveSeveralThenGetAllEntities() {
        File file1 = new File("title-1", "path-1");
        File file2 = new File("title-2", "path-2");
        File file3 = new File("title-3", "path-3");

        List.of(file3, file2, file1)
                .forEach(file -> fileRepository.save(file));

        Collection<File> expected = List.of(file1, file2, file3);

        Collection<File> fileRepositoryResponse = fileRepository.findAll();

        assertTrue(expected.size() == fileRepositoryResponse.size());
        assertTrue(expected.containsAll(fileRepositoryResponse));
    }

    @Test
    public void whenCreateCarWithOwnersListThenGetCorrectRepositoryResponse() {
        User user = User.builder()
                .email("email").password("password").build();
        userRepository.save(user);

        Post post = new Post(user, "test title", LocalDateTime.now());
        postRepository.save(post);

        File file1 = File.builder()
                .title("title-1").path("path-1").build();
        File file2 = File.builder()
                .title("title-2").path("path-2").build();
        File file3 = File.builder()
                .title("title-3").path("path-3").build();
        File file4 = File.builder()
                .title("title-4").path("path-4").build();
        List.of(file3, file1, file4, file1).forEach(
                file -> fileRepository.save(file));
        Collection<File> expFilesList =
                List.of(file4, file3, file2, file1);

        expFilesList.forEach(file -> {
                    post.addFile(file);
                    fileRepository.save(file);
                });

        Collection<File> responseRepository =
                fileRepository.findAllByUserId(post.getId());

        assertThat(responseRepository.size()).isEqualTo(expFilesList.size());
        assertThat(responseRepository.containsAll(expFilesList));
    }

}
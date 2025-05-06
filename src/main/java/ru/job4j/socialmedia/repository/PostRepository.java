package ru.job4j.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface PostRepository extends CrudRepository<Post, Integer> {

    Collection<Post> findByUser(User user);

    Collection<Post> findByCreatedGreaterThanEqualAndCreatedLessThanEqual(LocalDateTime startAt,
                                                                          LocalDateTime finishAt);

    Page<Post> findByOrderByCreatedDesc(Pageable pageable);

}

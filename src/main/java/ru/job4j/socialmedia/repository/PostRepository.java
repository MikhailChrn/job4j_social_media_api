package ru.job4j.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Collection<Post> findByUser(User user);

    Collection<Post> findByCreatedGreaterThanEqualAndCreatedLessThanEqual(LocalDateTime startAt,
                                                                          LocalDateTime finishAt);

    @Query(value = """
            SELECT *
            FROM posts p
            ORDER BY p.created DESC
            """, nativeQuery = true)
    Page<Post> findALLOrderByCreatedDesc(Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
            UPDATE posts p
            SET  p.title = :title, p.content = :content
            WHERE p.id = :id
            """, nativeQuery = true)
    int updateTitleAndContentById(@Param("id") int id,
                                  @Param("title") String title,
                                  @Param("content") String content);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
            DELETE FROM posts p
            WHERE p.id = :id
            """, nativeQuery = true)
    int deleteById(@Param("id") int id);

    Collection<Post> findByUserIdIn(Collection<Integer> userIds);
}

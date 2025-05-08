package ru.job4j.socialmedia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = """
            SELECT *
            FROM users u
            WHERE u.email = :email
            AND u.password = :pass
            """, nativeQuery = true)
    Optional<User> findByEmailAndPassword(@Param("email") String email,
                                          @Param("pass") String password);

    @Query(value = """
            SELECT *
            FROM users u
            WHERE u.id IN
                (SELECT s.user_id
                FROM subscribes s
                WHERE s.subscribe_id = :id)
            """, nativeQuery = true)
    Collection<User> findAllSubscribersByUserId(@Param("id") int userId);

    @Query(value = """
            SELECT *
            FROM users u
            WHERE u.id IN
                (SELECT f.user_to_id
                FROM friendships f
                WHERE f.user_from_id = :id)
            """, nativeQuery = true)
    Collection<User> findAllFriendsByUserId(@Param("id") int userId);
}

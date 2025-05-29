package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
            UPDATE users u
            SET u.email = :#{#user.email},
            u.password = :#{#user.password}
            WHERE u.id = :#{#user.id}
            """, nativeQuery = true)
    int update(@Param("user") User user);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = """
            DELETE
            FROM users u
            WHERE u.id = :id
            """, nativeQuery = true)
    int deleteById(@Param("id") int userId);

}

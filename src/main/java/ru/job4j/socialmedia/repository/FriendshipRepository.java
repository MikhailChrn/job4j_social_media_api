package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.entity.Friendship;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Modifying
    @Query(value = """
            DELETE
            FROM friendships f
            WHERE f.id = :id
            """, nativeQuery = true)
    int deleteById(@Param("id") int friendshipId);

    @Query(value = """
            SELECT *
            FROM friendships f
            WHERE s.user_from_id = :id
            """, nativeQuery = true)
    Collection<Friendship> findByUserFrom(@Param("id") int userFromId);

    @Query(value = """
            SELECT *
            FROM friendships f
            WHERE f.user_from_id = :fromId
            AND f.user_to_id = :toId
            """, nativeQuery = true)
    Optional<Friendship> findByUserFromAndUserTo(@Param("fromId") int userFromId,
                                                @Param("toId") int userToId);

    @Query(value = """
            SELECT *
            FROM friendships f
            WHERE f.user_to_id = :id
            """, nativeQuery = true)
    Collection<Friendship> findByUserTo(@Param("id") int userToId);
}

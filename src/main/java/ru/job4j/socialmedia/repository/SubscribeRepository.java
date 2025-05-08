package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.entity.Subscribe;

import java.util.Collection;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    @Modifying
    @Query(value = """
            DELETE
            FROM subscribes s
            WHERE s.id = :id
            """, nativeQuery = true)
    int deleteById(@Param("id") int subscribeId);

    @Query(value = """
            SELECT *
            FROM subscribes s
            WHERE s.user_id = :id
            """, nativeQuery = true)
    Collection<Subscribe> findByUserFrom(@Param("id") int userFromId);

    @Query(value = """
            SELECT *
            FROM subscribes s
            WHERE s.user_id = :fromId
            AND s.subscribe_id = :toId
            """, nativeQuery = true)
    Optional<Subscribe> findByUserFromAndUserTo(@Param("fromId") int userFromId,
                                                @Param("toId") int userToId);

    @Query(value = """
            SELECT *
            FROM subscribes s
            WHERE s.subscribe_id = :id
            """, nativeQuery = true)
    Collection<Subscribe> findByUserTo(@Param("id") int userToId);

}

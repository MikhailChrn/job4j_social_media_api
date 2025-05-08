package ru.job4j.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.socialmedia.entity.File;

import java.util.Collection;

public interface FileRepository extends JpaRepository<File, Integer> {

    @Query(value = """
            SELECT *
            FROM files f
            WHERE f.post_id = :id
            """, nativeQuery = true)
    Collection<File> findAllByUserId(@Param("id") Integer integer);

}

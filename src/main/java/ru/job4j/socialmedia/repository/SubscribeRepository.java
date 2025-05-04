package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.entity.Subscribe;

public interface SubscribeRepository extends CrudRepository<Subscribe, Integer> {
}

package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}

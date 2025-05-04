package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.entity.Friendship;

public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
}

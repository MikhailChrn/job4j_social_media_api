package ru.job4j.socialmedia.service;

import ru.job4j.socialmedia.entity.Friendship;
import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipService {

    void deleteAll();

    Collection<Friendship> findAll();

    Optional<Friendship> findById(int id);

    Optional<Friendship> findByUserFromAndUserTo(User userFrom, User userTo);

    boolean addFriendshipFromSubscribe(Subscribe subscribe);

    boolean addFriendshipBetweenUsers(User userFrom, User userTo);

    boolean removeFriendship(Friendship friendship);

    boolean removeFriendshipBetweenUsers(User userFrom, User userTo);

}

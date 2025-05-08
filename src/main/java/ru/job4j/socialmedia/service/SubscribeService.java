package ru.job4j.socialmedia.service;

import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface SubscribeService {

    Subscribe save(Subscribe subscribe);

    Optional<Subscribe> findById(int id);

    Optional<Subscribe> findByUserFromAndUserTo(User userFrom, User userTo);

    boolean deleteById(int id);

    void deleteAll();

    Collection<Subscribe> findAll();

    Collection<Subscribe> findByUserFrom(int id);

    Collection<Subscribe> findByUserTo(int id);

    boolean sendRequest(User userFrom, User subscribeTo);

    boolean callBackRequest(User userFrom, User subscribeTo);

}

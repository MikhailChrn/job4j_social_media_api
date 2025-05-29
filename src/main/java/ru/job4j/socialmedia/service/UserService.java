package ru.job4j.socialmedia.service;

import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<UserFullDto> findById(int id);

    Optional<UserFullDto> save(UserShortDto dto);

    boolean update(UserUpdateDto dto);

    boolean deleteById(int id);

    Collection<UserShortDto> findAll();
}

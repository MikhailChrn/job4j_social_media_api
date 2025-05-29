package ru.job4j.socialmedia.mapper;

import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;
import ru.job4j.socialmedia.entity.User;

import java.time.LocalDateTime;

public interface UserMapper {

    UserFullDto getDtoFromEntity(User user);

    UserShortDto getShortDtoFromEntity(User user);

    User getEntityFromDto(UserFullDto dto);

    User getEntityFromDtoWitoutCreate(UserShortDto dto);

    User getEntityFromUpdateDto(UserUpdateDto dto, LocalDateTime create);

}

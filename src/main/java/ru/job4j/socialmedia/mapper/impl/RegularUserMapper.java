package ru.job4j.socialmedia.mapper.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.mapper.UserMapper;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegularUserMapper implements UserMapper {

    @Override
    public UserFullDto getDtoFromEntity(User user) {
        return UserFullDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .create(user.getCreate())
                .build();
    }

    @Override
    public UserShortDto getShortDtoFromEntity(User user) {
        return UserShortDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public User getEntityFromDto(UserFullDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .create(dto.getCreate())
                .build();
    }

    @Override
    public User getEntityFromDtoWitoutCreate(UserShortDto dto) {
        return User.builder()
                .id(0)
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    @Override
    public User getEntityFromUpdateDto(UserUpdateDto dto, LocalDateTime create) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .create(create)
                .build();
    }
}

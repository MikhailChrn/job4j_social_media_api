package ru.job4j.socialmedia.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.User}
 */
@Value
@Builder
public class UserFullDto {

    int id;

    String email;

    String password;

    LocalDateTime create;

}

package ru.job4j.socialmedia.dto;

import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.User}
 */
@Value
@Builder
public class UserShortDto {

    String email;

    String password;

}
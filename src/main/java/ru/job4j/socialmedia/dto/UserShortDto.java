package ru.job4j.socialmedia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.User}
 */
@Value
@Builder
public class UserShortDto {

    @Email(message = "неверный формат email")
    String email;

    @NotBlank(message = "password не может быть пустым")
    String password;

}
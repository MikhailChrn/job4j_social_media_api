package ru.job4j.socialmedia.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.Post}
 */
@Value
@Builder
public class PostFullDto {

    int id;

    UserFullDto userFullDto;

    String title;

    String content;

    LocalDateTime created;

}

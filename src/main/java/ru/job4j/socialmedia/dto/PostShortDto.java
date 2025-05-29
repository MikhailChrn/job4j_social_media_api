package ru.job4j.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.Post}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostShortDto {

    UserShortDto userShortDto;

    String title;

    String content;

}

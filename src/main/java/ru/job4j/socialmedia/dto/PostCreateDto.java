package ru.job4j.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.socialmedia.entity.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {

    private User user;

    private String title;

    private String content;

}

package ru.job4j.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link ru.job4j.socialmedia.entity.Post}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostShortDto {

    UserShortDto userShortDto;

    @NotBlank(message = "title не может быть пустым")
    @Length(min = 3,
            message = "title должно быть не менее 3 символов")
    String title;

    @NotBlank(message = "content не может быть пустым")
    String content;

}

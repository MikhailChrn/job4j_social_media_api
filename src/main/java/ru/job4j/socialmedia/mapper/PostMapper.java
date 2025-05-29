package ru.job4j.socialmedia.mapper;

import ru.job4j.socialmedia.dto.*;
import ru.job4j.socialmedia.entity.Post;

import java.time.LocalDateTime;

public interface PostMapper {

    PostFullDto getDtoFromEntity(Post post);

    PostShortDto getShortDtoFromEntity(Post post);

    Post getEntityFromDto(PostFullDto dto);

}

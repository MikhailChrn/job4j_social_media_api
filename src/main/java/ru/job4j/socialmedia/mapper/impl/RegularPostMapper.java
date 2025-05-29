package ru.job4j.socialmedia.mapper.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmedia.dto.PostFullDto;
import ru.job4j.socialmedia.dto.PostShortDto;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.mapper.PostMapper;
import ru.job4j.socialmedia.mapper.UserMapper;
import ru.job4j.socialmedia.repository.UserRepository;

@Service
@AllArgsConstructor
public class RegularPostMapper implements PostMapper {

    private UserRepository userRepository;

    private UserMapper userMapper;

    @Override
    public PostFullDto getDtoFromEntity(Post post) {
        return PostFullDto.builder()
                .id(post.getId())
                .userFullDto(
                        userMapper.getDtoFromEntity(
                                post.getUser()))
                .title(post.getTitle())
                .content(post.getContent())
                .created(post.getCreated())
                .build();
    }

    @Override
    public PostShortDto getShortDtoFromEntity(Post post) {
        return PostShortDto.builder()
                .userShortDto(
                        userMapper.getShortDtoFromEntity(
                                post.getUser()))
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Override
    public Post getEntityFromDto(PostFullDto dto) {
        return Post.builder()
                .id(dto.getId())
                .user(
                        userRepository.findById(
                                dto.getId()).get())
                .title(dto.getTitle())
                .content(dto.getContent())
                .created(dto.getCreated())
                .build();
    }
}

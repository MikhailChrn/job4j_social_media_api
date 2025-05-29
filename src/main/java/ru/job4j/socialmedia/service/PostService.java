package ru.job4j.socialmedia.service;

import ru.job4j.socialmedia.dto.PostShortDto;
import ru.job4j.socialmedia.dto.PostFullDto;
import ru.job4j.socialmedia.dto.PostUpdateDto;
import ru.job4j.socialmedia.entity.File;

import java.util.Optional;

public interface PostService {

    Optional<PostFullDto> findById(Integer postId);

    Optional<PostFullDto> save(PostShortDto dto);

    boolean update(PostUpdateDto dto);

    boolean deleteById(int id);

    boolean addFileToPostById(int id, File file);

    boolean removeFileFromPostById(int postId, int fileId);

}

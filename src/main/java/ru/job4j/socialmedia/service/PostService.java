package ru.job4j.socialmedia.service;

import ru.job4j.socialmedia.dto.PostCreateDto;
import ru.job4j.socialmedia.dto.PostUpdateDto;
import ru.job4j.socialmedia.entity.File;

public interface PostService {

    boolean add(PostCreateDto dto);

    boolean update(PostUpdateDto dto);

    boolean deleteById(int id);

    boolean addFileToPostById(int id, File file);

    boolean removeFileFromPostById(int postId, int fileId);

}

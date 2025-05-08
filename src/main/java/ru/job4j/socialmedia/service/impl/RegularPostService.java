package ru.job4j.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.PostCreateDto;
import ru.job4j.socialmedia.dto.PostUpdateDto;
import ru.job4j.socialmedia.entity.File;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.repository.FileRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.service.PostService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegularPostService implements PostService {

    private PostRepository postRepository;

    private FileRepository fileRepository;

    /**
     * Добавляем новый пост на основании данных пользователя
     */
    @Transactional
    @Override
    public boolean add(PostCreateDto dto) {
        Post post = Post.builder()
                .user(dto.getUser())
                .title(dto.getTitle())
                .content(dto.getContent())
                .created(LocalDateTime.now())
                .build();
        postRepository.save(post);
        return post.getId() != 0;
    }

    /**
     * Обновляем новый пост на основании данных пользователя
     */
    @Transactional
    @Override
    public boolean update(PostUpdateDto dto) {
        return 0 != postRepository.updateTitleAndContentById(
                dto.getId(),
                dto.getTitle(),
                dto.getContent());
    }

    /**
     * Удаляем пост по ID
     */
    @Override
    public boolean deleteById(int id) {
        return 0 != postRepository.deleteById(id);
    }

    /**
     * Добавляем файл к посту
     */
    @Transactional
    @Override
    public boolean addFileToPostById(int id, File file) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            return false;
        }
        optionalPost.get().addFile(file);
        fileRepository.save(file);
        return true;
    }

    /**
     * Убираем файл из поста
     */
    @Transactional
    @Override
    public boolean removeFileFromPostById(int postId, int fileId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalPost.isEmpty()
                || optionalFile.isEmpty()
                || optionalFile.get().getPost().equals(optionalPost.get())) {
            return false;
        }
        optionalPost.get().removeFile(optionalFile.get());
        fileRepository.delete(optionalFile.get());
        return true;
    }
}

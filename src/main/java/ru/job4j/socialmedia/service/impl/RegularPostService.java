package ru.job4j.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.PostShortDto;
import ru.job4j.socialmedia.dto.PostFullDto;
import ru.job4j.socialmedia.dto.PostUpdateDto;
import ru.job4j.socialmedia.entity.File;
import ru.job4j.socialmedia.entity.Post;
import ru.job4j.socialmedia.mapper.PostMapper;
import ru.job4j.socialmedia.repository.FileRepository;
import ru.job4j.socialmedia.repository.PostRepository;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.PostService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegularPostService implements PostService {

    private PostRepository postRepository;

    private FileRepository fileRepository;

    private PostMapper postMapper;

    private final UserRepository userRepository;

    /**
     * Получаем представление публикации по ID.
     */
    @Override
    public Optional<PostFullDto> findById(Integer postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(postMapper
                .getDtoFromEntity(optionalPost.get()));
    }

    /**
     * Добавляем новый пост на основании данных пользователя
     */
    @Transactional
    @Override
    public Optional<PostFullDto> save(PostShortDto dto) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        Post post = Post.builder()
                .user(
                        userRepository.findByEmailAndPassword(
                                dto.getUserShortDto().getEmail(),
                                dto.getUserShortDto().getPassword()).get())
                .title(dto.getTitle())
                .content(dto.getContent())
                .created(now)
                .build();
        postRepository.save(post);
        return Optional.of(postMapper.getDtoFromEntity(post));
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

    @Override
    public Collection<PostShortDto> getPostsByUserIdIn(Collection<Integer> idUsers) {
        return postRepository.findByUserIdIn(idUsers).stream()
                .map(postMapper::getShortDtoFromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

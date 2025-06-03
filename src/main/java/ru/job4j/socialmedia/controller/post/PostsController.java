package ru.job4j.socialmedia.controller.post;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.socialmedia.dto.PostShortDto;
import ru.job4j.socialmedia.service.PostService;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostService postService;

    /**
     * Возвращает список из объектов <PostShortDto>
     * по запросу списка из user.id
     *
     * @return ResponseEntity.ok
     */
    @PostMapping("/byusers")
    public ResponseEntity<Collection<PostShortDto>> getPostsByUserIdIn(
            @RequestBody List<Integer> idUsers) {
        return ResponseEntity.ok()
                .body(postService.getPostsByUserIdIn(idUsers));
    }

}

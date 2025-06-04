package ru.job4j.socialmedia.controller.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmedia.dto.*;
import ru.job4j.socialmedia.service.PostService;

import java.net.URI;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    /**
     * @return 'ResponseEntity::ok' или 'ResponseEntity.notFound()'
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostFullDto> get(@PathVariable("postId")
                                           @NotNull
                                           @Min(value = 1,
                                                   message = "номер ресурса должен быть 1 и более")
                                           Integer postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @return 'HttpStatus.CREATED'
     */
    @PostMapping
    public ResponseEntity<PostFullDto> save(@Validated @RequestBody PostShortDto shortDto) {
        PostFullDto fullDto = postService.save(shortDto).get();
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fullDto.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(fullDto);
    }

    /**
     * PUT обычно требует полное дублирование
     * всех полей объекта вместе c изменённым:
     * непродублированные поля будут затёрты !
     * Этот метод ведёт себя как "присваивание".
     *
     * @return 'ResponseEntity.ok()' или 'ResponseEntity.notFound()'
     */
    @PutMapping
    public ResponseEntity<Void> update(@Validated @RequestBody PostUpdateDto dto) {
        if (postService.update(dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH обычно предоставляет возможность отправить несколько полей,
     * будет произведено слияние.
     *
     * @return 'HttpStatus.OK'
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@Validated @RequestBody PostUpdateDto dto) {
        postService.update(dto);
    }

    /**
     * @return 'ResponseEntity.noContent()' или 'HttpStatus.NOT_FOUND'
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable int id) {
        if (postService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}



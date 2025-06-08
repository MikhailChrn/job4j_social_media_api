package ru.job4j.socialmedia.controller.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "PostController", description = "PostController management APIs")
@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Retrieve a PostFullDto by postId",
            description = "Get a PostFullDto object by specifying its postId. The response is PostFullDto object with postId, title, content and date of created.",
            tags = { "Post", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostFullDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
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

    @Operation(
            summary = "Persist a Post from PostShortDto",
            description = "Create and persist a Post from passed PostShortDto. The response is PostFullDto object with postId, title, content and date of created.",
            tags = { "Post", "save" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostFullDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
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
    @Operation(
            summary = "Update a Post from PostUpdateDto",
            description = "Update a Post from passed PostUpdateDto. There are nothing to respond.",
            tags = { "Post", "update" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
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
    @Operation(
            summary = "Updating some fields of Post from PostUpdateDto",
            description = "Update some fields of a Post from passed PostUpdateDto. There are nothing to respond.",
            tags = { "Post", "change" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@Validated @RequestBody PostUpdateDto dto) {
        postService.update(dto);
    }

    @Operation(
            summary = "Delete Post by postId",
            description = "Delete Post from passed postId. There are nothing to respond.",
            tags = { "Post", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable int id) {
        if (postService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}



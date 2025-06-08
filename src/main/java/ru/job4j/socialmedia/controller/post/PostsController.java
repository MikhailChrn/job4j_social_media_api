package ru.job4j.socialmedia.controller.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Retrieve a list of PostShortDto by list of userId",
            description = "Get a list of PostShortDto object by list of userId. The response is list of PostShortDto objects.",
            tags = { "Post", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostShortDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/byusers")
    public ResponseEntity<Collection<PostShortDto>> getPostsByUserIdIn(
            @RequestBody List<Integer> idUsers) {
        return ResponseEntity.ok()
                .body(postService.getPostsByUserIdIn(idUsers));
    }

}

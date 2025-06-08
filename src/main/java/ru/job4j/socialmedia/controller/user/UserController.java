package ru.job4j.socialmedia.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmedia.dto.PostFullDto;
import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.service.UserService;

import java.net.URI;

@Tag(name = "UserController", description = "UserController management APIs")
@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Operation(
            summary = "Retrieve a User by userId",
            description = "Get a User object by specifying its userId. The response is User object with userId, username and date of created.",
            tags = { "User", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{userId}")
    public ResponseEntity<UserFullDto> get(@PathVariable("userId")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    Integer userId) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Persist a User from UserShortDto",
            description = "Create and persist a User from passed UserShortDto. The response is UserFullDto object with userId, email, password and date of created.",
            tags = { "User", "save" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostFullDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    public ResponseEntity<UserFullDto> save(@Validated @RequestBody UserShortDto shortDto) {
        UserFullDto fullDto = userService.save(shortDto).get();
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
     * PUT обычно требует полного дублирования всех полей объекта вместе c измененным:
     * непродублированные поля будут затёрты !
     * Этот метод ведёт себя как присваивание.
     *
     * @return 'ResponseEntity::ok' или 'ResponseEntity.notFound()'
     */
    @Operation(
            summary = "Update a User from UserUpdateDto",
            description = "Update a User from passed UserUpdateDto. There are nothing to respond.",
            tags = { "User", "update" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
    @PutMapping
    public ResponseEntity<Void> update(@Validated @RequestBody UserUpdateDto dto) {
        if (userService.update(dto)) {
            return ResponseEntity.ok()
                    .build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH обычно предоставляет возможность отправить несколько полей,
     * будет произведено слияние.
     *
     * @return 'ResponseEntity.noContent()' или 'HttpStatus.NOT_FOUND'
     */
    @Operation(
            summary = "Updating some fields of User from UserUpdateDto",
            description = "Update some fields of a User from passed UserUpdateDto. There are nothing to respond.",
            tags = { "User", "change" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@Validated @RequestBody UserUpdateDto dto) {
        userService.update(dto);
    }

    @Operation(
            summary = "Delete User by userId",
            description = "Delete User from passed userId. There are nothing to respond.",
            tags = { "User", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}) })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable int id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

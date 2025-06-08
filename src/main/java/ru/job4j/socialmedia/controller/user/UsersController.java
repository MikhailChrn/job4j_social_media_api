package ru.job4j.socialmedia.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.service.UserService;

import java.util.Collection;

@Tag(name = "UsersController", description = "UsersController management APIs")
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    /**
     * @return 'HttpStatus.OK'
     */
    @Operation(
            summary = "Retrieve all of UserShortDto",
            description = "Get All of UserShortDto objects. The response is list of UserShortDto objects.",
            tags = { "Post", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserShortDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserShortDto> getAll() {
        return userService.findAll();
    }
}

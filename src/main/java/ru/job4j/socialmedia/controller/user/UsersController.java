package ru.job4j.socialmedia.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.service.UserService;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserShortDto> getAll() {
        return userService.findAll();
    }
}

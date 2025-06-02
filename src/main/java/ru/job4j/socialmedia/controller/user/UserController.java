package ru.job4j.socialmedia.controller.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;
import ru.job4j.socialmedia.service.UserService;

import java.net.URI;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    /**
     * @return 'ResponseEntity::ok' или 'ResponseEntity.notFound()'
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserFullDto> get(@PathVariable("userId")
                                    @NotNull
                                    @Min(value = 1, message = "номер ресурса должен быть 1 и более")
                                    Integer userId) {
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * @return 'HttpStatus.CREATED'
     */
    @PostMapping
    public ResponseEntity<UserFullDto> save(@RequestBody UserShortDto shortDto) {
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
     * PUT требует полного дублирования всех полей объекта вместе c измененным:
     * непродублированные поля будут затёрты !
     * Этот метод ведёт себя как присваивание.
     *
     * @return 'ResponseEntity::ok' или 'ResponseEntity.notFound()'
     */
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserUpdateDto dto) {
        if (userService.update(dto)) {
            return ResponseEntity.ok()
                    .build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH предоставляет возможность отправить несколько полей,
     * будет произведено слияние.
     *
     * @return 'ResponseEntity.noContent()' или 'HttpStatus.NOT_FOUND'
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void change(@RequestBody UserUpdateDto dto) {
        userService.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable int id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

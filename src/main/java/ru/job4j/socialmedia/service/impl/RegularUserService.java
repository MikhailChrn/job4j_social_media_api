package ru.job4j.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.dto.UserFullDto;
import ru.job4j.socialmedia.dto.UserShortDto;
import ru.job4j.socialmedia.dto.UserUpdateDto;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.mapper.UserMapper;
import ru.job4j.socialmedia.repository.UserRepository;
import ru.job4j.socialmedia.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegularUserService implements UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    @Override
    public Optional<UserFullDto> findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper
                .getDtoFromEntity(optionalUser.get()));
    }

    /**
     * Добавляем нового пользователя на основе пользовательских данных
     * и текущего системного времени.
     * В ответ получаем представление пользователя с ID.
     */
    @Override
    public Optional<UserFullDto> save(UserShortDto dto) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        User user = userMapper.getEntityFromDtoWitoutCreate(dto);
        user.setCreate(now);
        userRepository.save(user);
        return Optional.of(userMapper.getDtoFromEntity(user));
    }

    /**
     * Обновить данные существующего пользователя.
     */
    @Transactional
    @Override
    public boolean update(UserUpdateDto dto) {
        LocalDateTime create = userRepository.findById(dto.getId()).get().getCreate();
        return userRepository.update(
                userMapper.getEntityFromUpdateDto(dto, create)) > 0L;
    }

    /**
     * Удалить пользователя по его ID.
     */
    @Override
    public boolean deleteById(int id) {
        return userRepository.deleteById(id) > 0L;
    }

    /**
     * Получить список ВСЕХ пользователей
     */
    @Override
    public Collection<UserShortDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::getShortDtoFromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

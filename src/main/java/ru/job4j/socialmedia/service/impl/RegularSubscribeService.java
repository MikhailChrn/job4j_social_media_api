package ru.job4j.socialmedia.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.repository.SubscribeRepository;
import ru.job4j.socialmedia.service.SubscribeService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegularSubscribeService implements SubscribeService {

    private final SubscribeRepository subscribeRepository;

    @Override
    public Subscribe save(Subscribe subscribe) {
        return subscribeRepository.save(subscribe);
    }

    @Override
    public Optional<Subscribe> findById(int id) {
        return subscribeRepository.findById(id);
    }

    @Override
    public Optional<Subscribe> findByUserFromAndUserTo(User userFrom, User userTo) {
        return subscribeRepository.findByUserFromAndUserTo(userFrom.getId(), userTo.getId());
    }

    @Override
    public boolean deleteById(int id) {
        return subscribeRepository.deleteById(id) > 0;
    }

    @Override
    public void deleteAll() {
        subscribeRepository.deleteAll();
    }

    @Override
    public Collection<Subscribe> findAll() {
        return subscribeRepository.findAll();
    }

    @Override
    public Collection<Subscribe> findByUserFrom(int id) {
        return subscribeRepository.findByUserFrom(id);
    }

    @Override
    public Collection<Subscribe> findByUserTo(int id) {
        return subscribeRepository.findByUserTo(id);
    }

    /**
     * Подписка является заявкой для создания дружбы.
     * Автор может создавать подписку и отзывать только сам.
     * После создания односторонней подписки пользователь-адресат видит её
     * и получает возможность добавить автора в друзья.
     * После создания "дружбы", автоматически создаётся встречная подписка.
     * Т.е "дружба" подразумевает наличие двух взаимных подписок между двумя пользователями.
     */
    @Override
    @Transactional
    public boolean sendRequest(User userFrom, User subscribeTo) {
        if (subscribeRepository.findByUserFromAndUserTo(userFrom.getId(), subscribeTo.getId()).isPresent()) {
            return false;
        }
        return subscribeRepository.save(Subscribe.builder()
                .userFrom(userFrom)
                .subscribeUserTo(subscribeTo)
                .created(LocalDateTime.now(ZoneId.of("UTC")))
                .build()).getId() != 0;
    }

    /**
     * Если один из пользователей удаляет со своей стороны "дружбу",
     * то он также удаляется его подписка на первого пользователя.
     * Второй пользователь при этом остаётся подписчиком.
     */
    @Override
    @Transactional
    public boolean callBackRequest(User userFrom, User userTo) {
        Optional<Subscribe> optionalSubscribe =
                subscribeRepository.findByUserFromAndUserTo(userFrom.getId(), userTo.getId());
        if (optionalSubscribe.isEmpty()) {
            return false;
        }
        return subscribeRepository.deleteById(optionalSubscribe.get().getId()) > 0;
    }
}

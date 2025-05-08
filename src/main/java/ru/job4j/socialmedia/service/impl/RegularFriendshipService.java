package ru.job4j.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.entity.Friendship;
import ru.job4j.socialmedia.entity.Subscribe;
import ru.job4j.socialmedia.entity.User;
import ru.job4j.socialmedia.repository.FriendshipRepository;
import ru.job4j.socialmedia.repository.SubscribeRepository;
import ru.job4j.socialmedia.service.FriendshipService;
import ru.job4j.socialmedia.service.SubscribeService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegularFriendshipService implements FriendshipService {

    private final SubscribeRepository subscribeRepository;

    private final FriendshipRepository friendshipRepository;

    private final SubscribeService subscribeService;

    @Override
    public void deleteAll() {
        friendshipRepository.deleteAll();
    }

    @Override
    public Collection<Friendship> findAll() {
        return friendshipRepository.findAll();
    }

    @Override
    public Optional<Friendship> findById(int id) {
        return friendshipRepository.findById(id);
    }

    @Override
    public Optional<Friendship> findByUserFromAndUserTo(User userFrom, User userTo) {
        return friendshipRepository.findByUserFromAndUserTo(userFrom.getId(), userTo.getId());
    }

    /**
     * Пользователи могут отправлять заявки в друзья другим пользователям.
     * С этого момента, пользователь, отправивший заявку, остается подписчиком до тех пор,
     * пока сам не откажется от подписки.
     * Если пользователь, получивший заявку, принимает ее, оба пользователя становятся друзьями.
     * Если отклонит, то пользователь, отправивший заявку, как и указано ранее, все равно остается подписчиком.
     */
    @Override
    @Transactional
    public boolean addFriendshipFromSubscribe(Subscribe subscribe) {
        if (subscribeService.findByUserFromAndUserTo(
                subscribe.getUserFrom(), subscribe.getSubscribeUserTo()).isPresent()) {
            return false;
        }
        return addFriendshipBetweenUsers(subscribe.getUserFrom(), subscribe.getSubscribeUserTo());
    }

    @Override
    @Transactional
    public boolean addFriendshipBetweenUsers(User userFrom, User userTo) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        Subscribe subscribeReverse = Subscribe.builder()
                .userFrom(userTo)
                .subscribeUserTo(userFrom)
                .created(now).build();
        subscribeRepository.save(subscribeReverse);

        Friendship main = Friendship.builder()
                .userFrom(userFrom)
                .userTo(userTo)
                .created(now).build();
        Friendship reverse = Friendship.builder()
                .userFrom(userTo)
                .userTo(userFrom)
                .created(now).build();
        friendshipRepository.save(main);
        friendshipRepository.save(reverse);

        return subscribeReverse.getId() != 0
                && main.getId() != 0
                && reverse.getId() != 0;
    }

    @Override
    @Transactional
    public boolean removeFriendship(Friendship friendship) {
        if (friendshipRepository.findByUserFromAndUserTo(
                friendship.getUserFrom().getId(), friendship.getUserTo().getId()).isEmpty()) {
            return false;
        }
        return removeFriendshipBetweenUsers(friendship.getUserFrom(), friendship.getUserTo());
    }

    /**
     * Если один из друзей удаляет другого из друзей, то он также отписывается.
     * Второй пользователь при этом должен остаться подписчиком.
     */
    @Override
    @Transactional
    public boolean removeFriendshipBetweenUsers(User userFrom, User userTo) {
        Optional<Subscribe> subscribe = subscribeRepository
                .findByUserFromAndUserTo(userFrom.getId(), userTo.getId());
        Optional<Friendship> main = friendshipRepository
                .findByUserFromAndUserTo(userFrom.getId(), userTo.getId());
        Optional<Friendship> reverse = friendshipRepository
                .findByUserFromAndUserTo(userTo.getId(), userFrom.getId());
        if (subscribe.isEmpty() || main.isEmpty() || reverse.isEmpty()) {
            return false;
        }

        if (subscribeRepository.deleteById(subscribe.get().getId()) > 0
                && friendshipRepository.deleteById(main.get().getId()) > 0
                && friendshipRepository.deleteById(reverse.get().getId()) > 0) {
            return true;
        }

        return false;
    }
}


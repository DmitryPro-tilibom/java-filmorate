package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new UserException("некорректный email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new UserException("некорректный логин");
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public User addFriend(Long userId,Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("{} теперь ваш друг", friend.getName());
        return user;
    }

    public void deleteFriend(Long userId, Long friendId) {
            User user = getUserById(userId);
            User friend = getUserById(friendId);
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            log.info("{} вам больше не друг", friend.getName());
    }

    public List<User> getFriendList(Long userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> commonFriends = getFriendList(userId);
        commonFriends.retainAll(getFriendList(friendId));
        return commonFriends;
    }
}

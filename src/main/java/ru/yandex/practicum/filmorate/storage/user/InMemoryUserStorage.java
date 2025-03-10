package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        log.info("Пользователь с именем {} добавлен", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) != null) {
            validateUser(user);
            loginCheck(user);
            users.put(user.getId(), user);
            log.info("Информация о пользователе {} изменёна", user.getName());
        } else {
            log.error("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    private void loginCheck(User user) {
        for (User existingUser : users.values()) {
            if (user.getLogin().equals(existingUser.getLogin())) {
                throw new ValidationException("Этот login уже используется другим пользователем");
            }
        }
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            throw new UserException("Email должен содержать @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new UserException("Login не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new UserException("Login не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserException("День рождения должен быть в прошлом");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return  ++currentMaxId;
    }
}


package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        log.info("Фильм {} добавлен", film.getName());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Информация о фильме {} обновлена", film.getName());
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        if (film == null || user == null) {
            throw new NotFoundException("Информация не найдена");
        }
        film.getLikes().add(userId);
        return film;
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (film != null) {
        if (!film.getLikes().contains(userId)) {
            log.info("Лайк пользователя {} не найден", userId);
            throw new NotFoundException("Лайк не найден");
        }
        film.getLikes().remove(userId);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public List<Film> getMostPopularFilms(String count) {
        if (count == null) {
            count = String.valueOf(10);
        }
        return getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(Long.parseLong(count)).collect(Collectors.toList());
    }
}

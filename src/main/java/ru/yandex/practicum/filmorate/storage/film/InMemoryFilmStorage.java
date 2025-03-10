package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Фильм с названием {} добавлен", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) != null) {
            validateFilm(film);
            films.put(film.getId(), film);
            log.info("Информация о фильме {} изменёна", film.getName());
        } else {
            log.error("Фильм не найден");
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new FilmException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не длинее 200 символов");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }

    private long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return  ++currentMaxId;
    }
}

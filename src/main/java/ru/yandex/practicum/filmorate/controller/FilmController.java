package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    @Getter
    static final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get all films");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
         log.info("Create film: {}", film);
        checkNewFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Update film: {}", film);
        checkNewFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkNewFilm(Film film){
        //название не может быть пустым;
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }

        //максимальная длина описания — 200 символов;
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        //дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        //продолжительность фильма должна быть положительным числом.
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

    }
}

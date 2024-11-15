package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get all films");
        return storageMap.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
         log.info("Create film: {}", film);
        checkNewFilm(film);
        film.setId(getNextId());
        storageMap.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Update film: {}", film);
        checkNewFilm(film);
        if (!storageMap.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
        storageMap.put(film.getId(), film);
        return film;
    }

    private void checkNewFilm(Film film) {

        //название не может быть пустым;
        if (film.getName() == null || film.getName().isBlank()) {
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

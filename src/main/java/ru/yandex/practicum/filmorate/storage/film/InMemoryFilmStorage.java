package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GeneratorId;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryFilmStorage extends GeneratorId implements FilmStorage {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public HashMap<Long, Film> filmsHash = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        log.info("Получен список фильмов.");
        return filmsHash.values();
    }

    @Override
    public Film addFilm(Film newFilm) {
        log.info("Create film: {}", newFilm);
        checkNewFilm(newFilm);
        newFilm.setId(getNextId(filmsHash));
        filmsHash.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Update film: {}", newFilm);
        checkNewFilm(newFilm);
        getFilm(newFilm.getId());
        filmsHash.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film getFilm(long filmId) {
        Film film = filmsHash.get(filmId);
        if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
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

    @Override
    public void addLike(Film film, long userId) {
        film.addLike(userId);
    }

    @Override
    public long getLikes(Film film) {
        return film.getLikes();
    }

    @Override
    public Set<Long> getLike(Film film) {
        return film.getUserIds();
    }

    @Override
    public void removeLike(Film film, long userId) {
        film.removeLike(userId);
    }

    @Override
    public List<Film> getTopFilms(long count) {
        return getFilms().stream()
                .sorted(Comparator.comparing(el -> (-1) * el.getLikes())).limit(count)
                .collect(Collectors.toList());
    }

}

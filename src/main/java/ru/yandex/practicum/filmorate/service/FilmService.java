package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    //пользователь ставит лайк фильму.
    public void addLike(long filmId, long userId) {
        userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        film.addLike(userId);
    }

    //пользователь удаляет лайк.
    public void delLike(long filmId, long userId) {
        userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        film.removeLike(userId);
    }

    // Метод возвращает список из первых count фильмов по количеству лайков.
    //  Если значение параметра count не задано, верните первые 10.
    public List<Film> getTopFilms() {
        return getTopFilms(10);
    }

    public List<Film> getTopFilms(long count) {
        Collection<Film> filmsHash = filmStorage.getFilms();
        return filmsHash.stream()
                .sorted(Comparator.comparing(el -> (-1) * el.getLikes())).limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getFilms() {
      return filmStorage.getFilms();
    }

    public Film addFilm(Film newFilm) {
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

}

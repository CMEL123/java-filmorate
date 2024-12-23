package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingDbStorage;

    //пользователь ставит лайк фильму.
    public void addLike(long filmId, long userId) {
        userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        filmStorage.addLike(film, userId);
    }

    //пользователь удаляет лайк.
    public void delLike(long filmId, long userId) {
        userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        filmStorage.removeLike(film, userId);
    }

    // Метод возвращает список из первых count фильмов по количеству лайков.
    //  Если значение параметра count не задано, верните первые 10.
    public List<Film> getTopFilms() {
        return getTopFilms(10);
    }

    public List<Film> getTopFilms(long count) {
        if (count < 0) throw new NotFoundException("Количество фильмов должно быть больше 0");
        return filmStorage.getTopFilms(count);
    }

    public Collection<Film> getFilms() {
      return filmStorage.getFilms();
    }

    public Film getFilm(long id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) throw new NotFoundException("Фильм с id = " + id + " не найден");
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        return film;
    }

    public Film addFilm(Film newFilm) {

        List<Integer> genres = genreStorage.getGenres().stream().map(el -> el.getId()).toList();
        for (Genre genre : newFilm.getGenres()) {
            if (!genres.contains(genre.getId())) {
                throw new NotFoundException("Жанра с id = " + genre.getId() + " не найден");
            }
        }

        if (newFilm.getMpa() != null && ratingDbStorage.getRating(newFilm.getMpa().getId()) == null) {
            throw new NotFoundException("Рейтинга с id = " + newFilm.getMpa().getId() + " не найден");
        }

        Film film = filmStorage.addFilm(newFilm);
        for (Genre genre : newFilm.getGenres()) {
            if (!film.getGenres().contains(genre)) {
                genreStorage.addGenres(film.getId(), genre.getId());
                film.addGenre(genre);
            }
        }

        return film;
    }

    public Film updateFilm(Film newFilm) {
        List<Integer> genres = genreStorage.getGenres().stream().map(el -> el.getId()).toList();
        for (Genre genre : newFilm.getGenres()) {
            if (!genres.contains(genre.getId())) {
                throw new NotFoundException("Жанра с id = " + genre.getId() + " не найден");
            }
        }

        if (newFilm.getMpa() != null && ratingDbStorage.getRating(newFilm.getMpa().getId()) == null) {
            throw new NotFoundException("Рейтинга с id = " + newFilm.getMpa().getId() + " не найден");
        }

        Film film = filmStorage.updateFilm(newFilm);
        for (Genre genre : newFilm.getGenres()) {
            if (!film.getGenres().contains(genre)) {
                genreStorage.addGenres(film.getId(), genre.getId());
                film.addGenre(genre);
            }
        }

        return film;
    }

    public long getLikes(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        return filmStorage.getLikes(film);
    }

}

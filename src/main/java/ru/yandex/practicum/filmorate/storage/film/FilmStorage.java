package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    public Film addFilm(Film film);

    public Film updateFilm(Film newFilm);

    public Film getFilm(long filmId);

    public Collection<Film> getFilms();

    public long getLikes(Film film);

    public Set<Long> getLike(Film film);

    public void addLike(Film film, long userId);

    public void removeLike(Film film, long userId);

    public List<Film> getTopFilms(long count);
}
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    //пользователь ставит лайк фильму.
    public void addLike(long filmId, long userId) {
        inMemoryUserStorage.getUser(userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
       film.addUserIds(userId);
    }

    //пользователь удаляет лайк.
    public void delLike(long filmId, long userId) {
        inMemoryUserStorage.getUser(userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        film.delUserIds(userId);
    }

    // Метод возвращает список из первых count фильмов по количеству лайков.
    //  Если значение параметра count не задано, верните первые 10.
    public List<Film> getTopFilms() {
        return getTopFilms(10);
    }

    public List<Film> getTopFilms(long count) {
        HashMap<Long, Film> filmsHash = inMemoryFilmStorage.getFilmsHash();
        List<Film> listSortLikeFilms = filmsHash.values().stream()
                .sorted(Comparator.comparing(el -> (-1) * el.getLikes()))
                .collect(Collectors.toList());
        List<Film> listOfPopularFilms = new ArrayList<>();
        if (listSortLikeFilms.size() > count) {
            for (int i = 0; i < count; i++) {
                listOfPopularFilms.add(listSortLikeFilms.get(i));
            }
        } else {
            listOfPopularFilms = listSortLikeFilms;
        }
        return listOfPopularFilms;
    }

}

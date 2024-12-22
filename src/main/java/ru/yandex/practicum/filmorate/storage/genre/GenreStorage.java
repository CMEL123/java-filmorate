package ru.yandex.practicum.filmorate.storage.genre;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.Map;

public interface GenreStorage {

    List<Genre> getFilmGenres(Long filmId);

    List<Map<Long, Genre>> getAllFilmsGenres();

    void addGenres(Long filmId, Integer genreId);

    List<Genre> getGenres();

    Genre getGenre(Integer genreId);

    void deleteFilmGenres(Long filmId);

}
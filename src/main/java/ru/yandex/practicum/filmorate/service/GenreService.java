package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.LinkedHashSet;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public LinkedHashSet<Genre> getAll() {
        return genreStorage.getGenres();
    }

    public Genre getById(Integer genreId) {
        Genre genre = genreStorage.getGenre(genreId);
        if (genre == null) throw new NotFoundException("Жанра с id = " + genreId + " не найден");
        return genre;
    }

}
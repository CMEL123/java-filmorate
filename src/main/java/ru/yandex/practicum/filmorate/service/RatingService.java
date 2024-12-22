package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;
@Service
public class RatingService {

    private final RatingStorage ratingStorage;

    public RatingService(RatingDbStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> getAll() {
        return ratingStorage.getRatings();
    }

    public Rating getById(Integer id) {
        Rating rating = ratingStorage.getRating(id);
        if (rating == null) throw new NotFoundException("Рейтинга с id = " + id + " не найден");
        return rating;
    }
}
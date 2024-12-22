package ru.yandex.practicum.filmorate.storage.rating;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;

public interface RatingStorage {

    List<Rating> getRatings();

    Rating getRating(Integer ratingId);

}

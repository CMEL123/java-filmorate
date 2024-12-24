package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = { "name", "releaseDate" })
public class Film {

    @ToString.Exclude
    long id; //целочисленный идентификатор;
    @NotBlank(message = "Название не может быть пустым")
    String name; //название
    @Size(max = 200, message = "Максимальная длина описания не должна превышать 200 символов.")
    String description; // описание — description;
    @NotNull
    LocalDate releaseDate; //дата релиза — releaseDate;
    @NotNull
    @Positive(message = "Продолжительность должна быть положительным числом")
    Integer duration;                    //продолжительность фильма — duration.
    @ToString.Exclude
    Set<Long> userIds = new HashSet<>();                   //Пользователи, которые поставили лайк
    @ToString.Exclude
    Rating mpa;
    @ToString.Exclude
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();


    public void addLike(long userId) {
        userIds.add(userId);
    }

    public void removeLike(long userId) {
        userIds.remove(userId);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}

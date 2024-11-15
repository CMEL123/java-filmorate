package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode
public class Film {
    long id; //целочисленный идентификатор;
    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    String name; //название
    @Size(max = 200, message = "Максимальная длина описания не должна превышать 200 символов.")
    String description; // описание — description;
    @NotNull
    LocalDate releaseDate; //дата релиза — releaseDate;
    @NotNull
    @Positive(message = "Продолжительность должна быть положительным числом")
    Integer duration; //продолжительность фильма — duration.
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Film.
 */
@Data
@EqualsAndHashCode
public class Film {
    long id; //целочисленный идентификатор;
    String name; //название
    String description; // описание — description;
    LocalDate releaseDate; //дата релиза — releaseDate;
    Integer duration; //продолжительность фильма — duration.
}

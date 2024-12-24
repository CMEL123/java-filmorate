package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Genre {

    @NotNull
    Integer id;
    @NotBlank(message = "Жанр не может быть пустым и содержать пробелы")
    String name;

}
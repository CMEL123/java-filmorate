package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Genre {

    @NotNull
    Integer id;
    @NotBlank(message = "Жанр не может быть пустым и содержать пробелы")
    String name;

}
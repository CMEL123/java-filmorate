package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Rating {

    @NotNull
    Integer id;
    String name;

}
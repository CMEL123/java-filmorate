package ru.yandex.practicum.filmorate.model;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {

    @NotNull
    Integer id;
    String name;

}
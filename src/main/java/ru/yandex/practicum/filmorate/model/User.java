package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@EqualsAndHashCode
public class User {
    long id; //целочисленный идентификатор — id;
    String email; //электронная почта — email;
    String login; //логин пользователя — login;
    String name; //имя для отображения — name;
    LocalDate birthday; //дата рождения — birthday.
}

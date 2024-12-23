package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class User {

    long id; //целочисленный идентификатор — id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    String email; //электронная почта — email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    String login; //логин пользователя — login;

    String name; //имя для отображения — name;

    @NotNull(message = "Дата рождения не может быть пустым.")
    @Past(message = "Дата рождения не может быть в будущем.")
    LocalDate birthday; //дата рождения — birthday.

    Set<Long> friendIds = new HashSet<>(); //друзья

    public void addFriendIds(long userId) {
        friendIds.add(userId);
    }

    public void delFriendIds(long userId) {
        friendIds.remove(userId);
    }

}

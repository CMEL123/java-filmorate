package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Getter
    static final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get all users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Create user: {}", user);
        checkNewUser(user);
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Update user: {}", user);
        checkNewUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Фильм с id = " + user.getId() + " не найден");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private User checkNewUser(User user){
        //электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
           log.warn("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        //дата рождения не может быть в будущем.
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        return user;
    }
}

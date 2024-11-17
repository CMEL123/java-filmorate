package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Collection;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller {

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get all users");
        return storageMap.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Create user: {}", user);
        user = checkNewUser(user);

        user.setId(getNextId());
        storageMap.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Update user: {}", user);
        checkNewUser(user);
        if (!storageMap.containsKey(user.getId())) {
            throw new ValidationException("Фильм с id = " + user.getId() + " не найден");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        storageMap.put(user.getId(), user);
        return user;
    }

    private User checkNewUser(User user) {
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        //электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
           log.warn("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        //дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        return user;
    }
}

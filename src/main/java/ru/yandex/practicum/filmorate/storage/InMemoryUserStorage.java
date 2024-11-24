package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage extends GeneratorId implements UserStorage {
    @Getter
    public HashMap<Long, User> usersHash = new HashMap<>();

    public Collection<User> getUsers() {
        log.info("Получен список пользователей.");
        return usersHash.values();
    }

    @Override
    public User addUser(User newUser) {
        log.info("Create user: {}", newUser);
        newUser = checkNewUser(newUser);

        newUser.setId(getNextId(usersHash));
        usersHash.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Update user: {}", newUser);
        checkNewUser(newUser);
        getUser(newUser.getId());
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        usersHash.put(newUser.getId(), newUser);
        return newUser;
    }

    public User getUser(long userId) {
        User user = usersHash.get(userId);
        if (user == null) throw new NotFoundException("Нет пользователя с id: " + userId);
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

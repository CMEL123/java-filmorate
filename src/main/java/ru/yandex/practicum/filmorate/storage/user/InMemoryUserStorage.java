package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryUserStorage extends GeneratorId implements UserStorage {

    public HashMap<Long, User> usersHash = new HashMap<>();

    public Collection<User> getUsers() {
        log.info("Получен список пользователей.");
        return usersHash.values();
    }

    @Override
    public User addUser(User newUser) {
        log.info("Create user: {}", newUser);
        checkNewUser(newUser);

        newUser.setId(getNextId(usersHash));
        usersHash.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Update user: {}", newUser);
        checkNewUser(newUser);
        getUser(newUser.getId());
        usersHash.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUser(long userId) {
        User user = usersHash.get(userId);
        if (user == null) throw new NotFoundException("Нет пользователя с id: " + userId);
        return user;
    }

    private void checkNewUser(User user) {
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

    }

    @Override
    public List<User> getFriends(long userId) {
        User user = getUser(userId);
        return user.getFriendIds().stream().map(this::getUser).collect(Collectors.toList());
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.addFriendIds(friendId);
        friend.addFriendIds(userId);
    }

    @Override
    public void delFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.delFriendIds(friendId);
        friend.delFriendIds(userId);
    }

    @Override
    public List<User> getMutualFriends(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        return user.getFriendIds().stream()
                .filter(friend.getFriendIds()::contains)
                .map(this::getUser).collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    //получить друзей
    public List<User> getFriends(long userId) {
        return userStorage.getFriends(userId);
    }

    //добавить друга
    public void addFriend(long userId, long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    //удалить друга
    public void delFriend(long userId, long friendId) {
        userStorage.delFriend(userId, friendId);
    }

    // получить общих друзей
    public List<User> getMutualFriends(long userId, long friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User newUser) {
        return userStorage.addUser(newUser);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

}

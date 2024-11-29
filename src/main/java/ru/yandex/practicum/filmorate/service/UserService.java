package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    //получить друзей
    public List<User> getFriends(long userId) {
        User user = userStorage.getUser(userId);
        return user.getFriendIds().stream().map(userStorage::getUser).collect(Collectors.toList());
    }

    //добавить друга
    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.addFriendIds(friendId);
        friend.addFriendIds(userId);
    }

    //удалить друга
    public void delFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.delFriendIds(friendId);
        friend.delFriendIds(userId);
    }

    // получить общих друзей
    public List<User> getMutualFriends(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        return user.getFriendIds().stream()
                .filter(friend.getFriendIds()::contains)
                .map(userStorage::getUser).collect(Collectors.toList());
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User newUser) {
        return userStorage.addUser(newUser);
    }

    public User updateUser(User newUser) {
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (newUser.getName().isEmpty()) {
            newUser.setName(newUser.getLogin());
        }
        return userStorage.updateUser(newUser);
    }

}

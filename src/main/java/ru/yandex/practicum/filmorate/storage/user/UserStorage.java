package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    public Collection<User> getUsers();

    public User addUser(User film);

    public User updateUser(User newUser);

    public User getUser(long userId);

    void addFriend(long userId, long friendId);

    void delFriend(long userId, long friendId);

    List<User> getFriends(long userId);

    List<User> getMutualFriends(long userId, long friendId);
}
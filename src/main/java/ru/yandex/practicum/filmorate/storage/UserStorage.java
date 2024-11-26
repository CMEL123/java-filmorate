package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {

    public Collection<User> getUsers();

    public User addUser(User film);

    public User updateUser(User newUser);

    public User getUser(long userId);

    public HashMap<Long, User> getUsersHash();
}
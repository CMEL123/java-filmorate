package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    //получить друзей
    public List<User> getFriends(long userId) {
        HashMap<Long, User> usersHash = inMemoryUserStorage.getUsersHash();
        User user = inMemoryUserStorage.getUser(userId);
        return user.getFriendIds().stream().map(usersHash::get).collect(Collectors.toList());
    }

    //добавить друга
    public void addFriend(long userId, long friendId) {
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        user.addFriendIds(friendId);
        friend.addFriendIds(userId);
    }

    //удалить друга
    public void delFriend(long userId, long friendId) {
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        user.delFriendIds(friendId);
        friend.delFriendIds(userId);
    }

    // получить общих друзей
    public List<User> getMutualFriends(long userId, long friendId) {
        HashMap<Long, User> usersHash = inMemoryUserStorage.getUsersHash();
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);

        return user.getFriendIds().stream()
                .filter(friend.getFriendIds()::contains)
                .map(usersHash::get).collect(Collectors.toList());
    }


}

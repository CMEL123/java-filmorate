package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("/users");
        return userService.getUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("/users. Create");
        return userService.addUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("/users. Update");
        return userService.updateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Put. /users/{" + id + "}/friends/{" + friendId + "}");
        userService.addFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void delFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Delete. /users/{" + id + "}/friends/{" + friendId + "}");
        userService.delFriend(id, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("Get. /users/{" + id + "}/friends/");
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Get. /users/{" + id + "}/friends/common/{" + otherId + "}");
        return userService.getMutualFriends(id, otherId);
    }

}

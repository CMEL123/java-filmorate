package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final JdbcTemplate jdbcTemplate;
    UserController userController;

    static User user = new User();

    @BeforeEach
    public void beforeEach() {
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);

        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update(FileReader.readString("src/test/resources/drop.sql"));
    }

    @Test
    public void testCreateAndAllUsers() {
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testUpdateAndAllUsers() {
        userController.create(user);
        User newUser = userController.findAll().stream().findFirst().get();
        assertEquals(newUser, user);
        assertEquals(1, userController.findAll().size());

        User user2 = new User();
        user2.setId(newUser.getId());
        user2.setName("Test2");
        user2.setEmail("1@asd.rt");
        user2.setLogin("TestLogin");
        user2.setBirthday(LocalDate.now());
        userController.update(user2);
        assertEquals(userController.findAll().stream().findFirst().get(), user2);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testBadUpdate() {
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());

        User user2 = new User();
        user2.setId(99);
        user2.setName("Test2");
        user2.setEmail("1@asd.rt");
        user2.setLogin("TestLogin");
        user2.setBirthday(LocalDate.now());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            userController.update(user2);
        });
        assertEquals("Нет пользователя с id: " + user2.getId(), thrown.getMessage());
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullName() {
        user.setName(null);
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(userController.findAll().stream().findFirst().get().getName(), user.getLogin());
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithEmptyEmail() {
        user.setEmail("");
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadEmail() {
        user.setEmail("asd");
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

      //  assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullEmail() {
        user.setEmail(null);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullLogin() {
        user.setLogin(null);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadLogin() {
        user.setLogin("Test test");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadBirthday() {
        user.setBirthday(LocalDate.now().plusDays(10));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testAddFriend() {
        User user2 = new User();
        user2.setId(99);
        user2.setName("Test2");
        user2.setEmail("1@asd.rt");
        user2.setLogin("TestLogin2");
        user2.setBirthday(LocalDate.now());

        userController.create(user);
        userController.create(user2);

        userController.addFriend(user.getId(), user2.getId());
        assertEquals(1, userController.getFriends(user.getId()).size());
        assertEquals(0, userController.getFriends(user2.getId()).size());
        assertTrue(userController.getFriends(user.getId()).contains(user2));
        assertFalse(userController.getFriends(user2.getId()).contains(user));
    }

    @Test
    public void testDelFriend() {
        User user2 = new User();
        user2.setId(99);
        user2.setName("Test2");
        user2.setEmail("1@asd.rt");
        user2.setLogin("TestLogin2");
        user2.setBirthday(LocalDate.now());

        userController.create(user);
        userController.create(user2);

        userController.addFriend(user.getId(), user2.getId());
        userController.delFriend(user.getId(), user2.getId());
        assertEquals(0, userController.getFriends(user.getId()).size());
        assertEquals(0, userController.getFriends(user2.getId()).size());
        assertFalse(userController.getFriends(user.getId()).contains(user2));
        assertFalse(userController.getFriends(user2.getId()).contains(user));
    }

}

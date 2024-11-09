package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @AfterEach
    public void afterEach() {
        UserController.getUsers().clear();
    }

    @Test
    public void testCreateAndAllUsers() {
        User user = new User();
        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testUpdateAndAllUsers() {
        User user = new User();
        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());

        User user2 = new User();
        user2.setId(user.getId());
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
        User user = new User();
        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());

        User user2 = new User();
        user2.setId(99);
        user2.setName("Test2");
        user2.setEmail("1@asd.rt");
        user2.setLogin("TestLogin");
        user2.setBirthday(LocalDate.now());

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.update(user2);
        });
        assertEquals("Фильм с id = " + user2.getId() + " не найден", thrown.getMessage());
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullName() {
        User user = new User();
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());
        userController.create(user);
        assertEquals(userController.findAll().stream().findFirst().get(), user);
        assertEquals(userController.findAll().stream().findFirst().get().getName(), user.getLogin());
        assertEquals(1, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithEmptyEmail() {
        User user = new User();
        user.setEmail("");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadEmail() {
        User user = new User();
        user.setEmail("asda");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullEmail() {
        User user = new User();

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithNullLogin() {
        User user = new User();
        user.setEmail("1@asd.rt");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadLogin() {
        User user = new User();
        user.setEmail("1@asd.rt");
        user.setLogin("Test test");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

    @Test
    public void testCreateUserWithBadBirthday() {
        User user = new User();
        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now().plusDays(10));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage());
        assertEquals(0, userController.findAll().size());
    }

}

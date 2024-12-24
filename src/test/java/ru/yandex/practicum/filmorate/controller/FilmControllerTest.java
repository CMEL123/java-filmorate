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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {

    FilmController filmController;
    UserController userController;

    private final JdbcTemplate jdbcTemplate;
    static Film film = new Film();
    static User user = new User();

    @BeforeEach
    public void beforeEach() {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        UserStorage userStorage = new UserDbStorage(jdbcTemplate);
        GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
        RatingStorage ratingStorage = new RatingDbStorage(jdbcTemplate);


        FilmService filmService = new FilmService(filmStorage, userStorage, genreStorage, ratingStorage);
        UserService userService = new UserService(userStorage);

        filmController = new FilmController(filmService);
        userController = new UserController(userService);

        film.setName("Test");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        user.setName("Test");
        user.setEmail("1@asd.rt");
        user.setLogin("TestLogin");
        user.setBirthday(LocalDate.now());

        userController.create(user);
        System.out.println(user);
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update(FileReader.readString("src/test/resources/drop.sql"));
    }

    @Test
    public void testCreateAndAllFilms() {
        filmController.create(film);
        assertEquals(filmController.findAll().stream().findFirst().get(), film);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    public void testUpdateAndAllFilms() {
        filmController.create(film);
        assertEquals(filmController.findAll().stream().findFirst().get(), film);
        assertEquals(1, filmController.findAll().size());

        Film film2 = new Film();
        film2.setId(film.getId());
        film2.setName("Test2");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);
        filmController.update(film2);
        assertEquals(filmController.findAll().stream().findFirst().get(), film2);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    public void testBadUpdate() {

        filmController.create(film);
        assertEquals(filmController.findAll().stream().findFirst().get(), film);
        assertEquals(1, filmController.findAll().size());

        Film film2 = new Film();
        film2.setId(99);
        film2.setName("Test2");
        film2.setDescription("Description");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            filmController.update(film2);
        });
        assertEquals("Фильм с id = " + film2.getId() + " не найден", thrown.getMessage());
        assertEquals(filmController.findAll().stream().findFirst().get(), film);
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    public void testCreateFilmWithNullName() {
        film.setName(null);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });

        assertEquals("Название не может быть пустым", thrown.getMessage());
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void testCreateFilmWithEmptyName() {
        film.setName("");

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });

        assertEquals("Название не может быть пустым", thrown.getMessage());
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void testCreateFilmWithBadDescription() {

        film.setDescription("*".repeat(201));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });

        assertEquals("Максимальная длина описания — 200 символов", thrown.getMessage());
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void testCreateFilmWithBadReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", thrown.getMessage());
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void testCreateFilmWithBadDuration() {

        film.setDuration(-1);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });

        assertEquals("Продолжительность фильма должна быть положительным числом", thrown.getMessage());
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void testAddLike() {
        filmController.create(film);
        filmController.addLike(film.getId(), user.getId());
        assertEquals(filmController.getLikes(film.getId()), 1);
        filmController.addLike(film.getId(), user.getId());
        assertEquals(filmController.getLikes(film.getId()), 1);
    }

    @Test
    public void testDelLike() {
        filmController.create(film);
        filmController.addLike(film.getId(), user.getId());
        assertEquals(filmController.getLikes(film.getId()), 1);
        filmController.delLike(film.getId(), user.getId());
        assertEquals(filmController.getLikes(film.getId()), 0);
    }
}
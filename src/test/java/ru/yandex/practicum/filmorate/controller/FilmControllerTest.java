package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    static Film film = new Film();

    @BeforeEach
    public void BeforeEach() {
        film.setName("Test");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);
    }

    @AfterEach
    public void afterEach() {
        filmController.getStorageMap().clear();
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

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
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
}
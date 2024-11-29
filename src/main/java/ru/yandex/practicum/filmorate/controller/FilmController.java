package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("/films");
        return filmService.getFilms();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("/films. Create");
        return filmService.addFilm(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("/films. Update");
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Put. /films/{" + id + "}/like/{" + userId + "}");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void delLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete. /films/{" + id + "}/like/{" + userId + "}");
        filmService.delLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Get. /films/popular?count={" + count + "}");
        return filmService.getTopFilms(count);
    }
}

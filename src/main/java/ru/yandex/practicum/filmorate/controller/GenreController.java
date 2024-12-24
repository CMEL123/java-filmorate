package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.LinkedHashSet;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/genres")
    public LinkedHashSet<Genre> findAll() {
        return genreService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/genres/{id}")
    public Genre findById(@PathVariable Integer id) {
        return genreService.getById(id);
    }

}
package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/mpa")
    public List<Rating> findAll() {
        return ratingService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/mpa/{id}")
    public Rating findById(@PathVariable Integer id) {
        return ratingService.getById(id);
    }

}

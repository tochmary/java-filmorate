package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //GET /films — для получения списка фильмов.
    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    //GET /films/{id} — для получение фильма по id.
    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return filmService.findById(id);
    }


    //POST /films — для добавления нового фильма в список.
    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmService.create(film);
    }

    //PUT /films — обновление значения полей фильм существующего.
    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        return filmService.update(film);
    }

    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        return filmService.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    //GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopularFilms(count);
    }
}

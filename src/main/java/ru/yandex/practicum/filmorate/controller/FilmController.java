package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    //GET /films — для получения списка фильмов.
    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    //POST /films — для добавления нового фильма в список.
    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        Validation.checkFilm(film);
        int idFilm = generateId();
        film.setId(idFilm);
        films.put(idFilm, film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    //PUT /films — обновление значения полей фильм существующего.
    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        Integer idFilm = film.getId();
        Validation.checkFilm(film);
        if (isExistFilm(film)) {
            films.put(idFilm, film);
            log.debug("Обновлен пользователь: {}", film);
        } else {
            throw new ValidationException("Фильма с id = " + idFilm + " не существует!");
        }
        return film;
    }

    private int generateId() {
        return id++;
    }

    private boolean isExistFilm(Film film) {
        return films.containsKey(film.getId());
    }
}

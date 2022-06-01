package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAll();

    Optional<Film> findById(Integer id);

    Film create(Film film);

    Film update(Film film);

    void addLike(Film film, Integer userId);

    void deleteLike(Film film, Integer userId);

    boolean isFilmExist(Integer id);
}

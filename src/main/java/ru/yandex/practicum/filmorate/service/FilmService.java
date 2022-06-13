package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public static void checkFilm(Film film) {
        String name = film.getName();
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная фильма длина описания — 200 символов!");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года!");
        }
        if (film.getDuration() != null && film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
    }

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Фильма с id = " + id + " не существует!")
        );
    }

    public Film create(Film film) {
        checkFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        checkFilm(film);
        checkFilmForExist(film.getId());
        return filmStorage.update(film);
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        userService.checkUserForExist(userId);
        if (!film.getLike().contains(userId)) {
            filmStorage.addLike(film, userId);
        }
        return film;
    }

    public Film deleteLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        userService.checkUserForExist(userId);
        if (film.getLike().contains(userId)) {
            filmStorage.deleteLike(film, userId);
        }
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findAll()
                .stream()
                .sorted()
                .limit(count)
                .collect(Collectors.toList());
    }

    public void checkFilmForExist(Integer id) {
        if (!filmStorage.isFilmExist(id)) {
            throw new NotFoundException("Фильма с id = " + id + " не существует!");
        }
    }
}

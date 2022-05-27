package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    public Film findById(Integer id) {
        return films.get(id);
    }

    public Film create(Film film) {
        int idFilm = generateId();
        film.setId(idFilm);
        films.put(idFilm, film);
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Обновлен пользователь: {}", film);
        return film;
    }

    public boolean isExistFilm(Integer id) {
        return films.containsKey(id);
    }

    private int generateId() {
        return id++;
    }
}

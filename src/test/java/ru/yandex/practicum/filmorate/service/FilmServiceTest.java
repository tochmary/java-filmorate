package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmServiceTest {
    private final Film film = new Film();
    private static final String MESSAGE_FILM_NAME = "Название фильма не может быть пустым!";
    private static final String MESSAGE_FILM_DESC = "Максимальная фильма длина описания — 200 символов!";
    private static final String MESSAGE_FILM_RELEASE_DATE = "Дата релиза — не раньше 28 декабря 1895 года!";
    private static final String MESSAGE_FILM_DURATION = "Продолжительность фильма должна быть положительной!";

    @BeforeEach
    void setUp() {
        film.setName("Titanic");
        film.setDescription("About ship Titanic");
        film.setDuration(180);
        film.setReleaseDate(LocalDate.of(1998, 1, 1));
    }

    @Test
    @DisplayName("Проверка фильма")
    void createFilm() throws ValidationException {
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным именем")
    void createFilmNullName() {
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c пустым именем")
    void createFilmEmptyName() {
        film.setName("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным описанием")
    void createFilmDescNull() throws ValidationException {
        film.setDescription(null);
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c описанием равным 200 символов")
    void createFilmDesc200() throws ValidationException {
        String newDesc = "Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts.";
        film.setDescription(newDesc);
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c описанием больше 200 символов")
    void createFilmDescMore200() {
        film.setDescription("Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts of the sinking of the RMS Titanic");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DESC, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным днем релиза")
    void createFilmReleaseDateNull() throws ValidationException {
        film.setReleaseDate(null);
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c днем релиза равным 28.12.1895")
    void createFilmReleaseDate28121895() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c днем релиза раньше 28.12.1895")
    void createFilmReleaseDateLess28121895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_RELEASE_DATE, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненной продолжительностью")
    void createFilmDurationNull() throws ValidationException {
        film.setDuration(null);
        FilmService.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c продолжительностью равной 0")
    void createFilmDurationEqual0() {
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c продолжительностью меньше 0")
    void createFilmDurationLess0() {
        film.setDuration(-20);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> FilmService.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }
}
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

class FilmControllerTest {
    private FilmController filmController;
    private final Film film = new Film();
    private static final String messageName = "Название фильма не может быть пустым!";
    private static final String messageDesc = "Максимальная фильма длина описания — 200 символов!";
    private static final String messageReleaseDate = "Дата релиза — не раньше 28 декабря 1895 года!";
    private static final String messageDuration = "Продолжительность фильма должна быть положительной!";

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        film.setName("Titanic");
        film.setDescription("About ship Titanic");
        film.setDuration(180);
        film.setReleaseDate(LocalDate.of(1998, 1, 1));
    }

    @Test
    @DisplayName("Пустой список фильмов")
    void findAllEmptyFilms() {
        Assertions.assertEquals(0, filmController.findAll().size(),
                "Непустое количество фильмов!");
    }

    @Test
    @DisplayName("Непустой список фильмов")
    void findAllNotEmptyFilms() throws ValidationException {
        filmController.create(film);
        Assertions.assertNotEquals(0, filmController.findAll().size(),
                "Пустое количество фильмов!");
    }

    @Test
    @DisplayName("Создание пустого фильма")
    void createEmptyFilm() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(new Film()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма")
    void createFilm() throws ValidationException {
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c незаполненным именем")
    void createFilmNullName() {
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageName, exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма c пустым именем")
    void createFilmEmptyName() {
        film.setName("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageName, exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма c незаполненным описанием")
    void createFilmDescNull() throws ValidationException {
        film.setDescription(null);
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertNull(film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c описанием равным 200 символов")
    void createFilmDesc200() throws ValidationException {
        String newDesc = "Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts.";
        film.setDescription(newDesc);
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals(newDesc, film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c описанием больше 200 символов")
    void createFilmDescMore200() {
        film.setDescription("Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts of the sinking of the RMS Titanic");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageDesc, exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма c незаполненным днем релиза")
    void createFilmReleaseDateNull() throws ValidationException {
        film.setReleaseDate(null);
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertNull(film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c днем релиза равным 28.12.1895")
    void createFilmReleaseDate28121895() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1895, 12, 28), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c днем релиза раньше 28.12.1895")
    void createFilmReleaseDateLess28121895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageReleaseDate, exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма c незаполненной продолжительностью")
    void createFilmDurationNull() throws ValidationException {
        film.setDuration(null);
        filmController.create(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertNull(film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Создание фильма c продолжительностью равной 0")
    void createFilmDurationEqual0() {
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageDuration, exception.getMessage());
    }

    @Test
    @DisplayName("Создание фильма c продолжительностью меньше 0")
    void createFilmDurationLess0() {
        film.setDuration(-20);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(messageDuration, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление пустого фильма")
    void updateEmptyFilm() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.create(new Film()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма")
    void updateFilm() throws ValidationException {
        filmController.create(film);
        film.setName("Titanic2");
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic2", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c незаполненным именем")
    void updateFilmNullName() throws ValidationException {
        filmController.create(film);
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageName, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма c пустым именем")
    void updateFilmEmptyName() throws ValidationException {
        filmController.create(film);
        film.setName("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageName, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма c незаполненным описанием")
    void updateFilmDescNull() throws ValidationException {
        filmController.create(film);
        film.setDescription(null);
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertNull(film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c описанием равным 200 символов")
    void updateFilmDesc200() throws ValidationException {
        filmController.create(film);
        String newDesc = "Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts.";
        film.setDescription(newDesc);
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals(newDesc, film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c описанием больше 200 символов")
    void updateFilmDescMore200() throws ValidationException {
        filmController.create(film);
        film.setDescription("Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts of the sinking of the RMS Titanic");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageDesc, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма c незаполненным днем релиза")
    void updateFilmReleaseDateNull() throws ValidationException {
        filmController.create(film);
        film.setReleaseDate(null);
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertNull(film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c днем релиза равным 28.12.1895")
    void updateFilmReleaseDate28121895() throws ValidationException {
        filmController.create(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertEquals(180, film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1895, 12, 28), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c днем релиза раньше 28.12.1895")
    void updateFilmReleaseDateLess28121895() throws ValidationException {
        filmController.create(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageReleaseDate, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма c незаполненной продолжительностью")
    void updateFilmDurationNull() throws ValidationException {
        filmController.create(film);
        film.setDuration(null);
        filmController.update(film);
        Film film1 = filmController.findAll().get(0);
        Assertions.assertEquals("Titanic", film1.getName(),
                "Некорректное имя фильма");
        Assertions.assertEquals("About ship Titanic", film1.getDescription(),
                "Некорректное описание фильма");
        Assertions.assertNull(film1.getDuration(),
                "Некорректная продолжительность фильма");
        Assertions.assertEquals(LocalDate.of(1998, 1, 1), film1.getReleaseDate(),
                "Некорректный день релиза фильма");
    }

    @Test
    @DisplayName("Обновление фильма c продолжительностью равной 0")
    void updateFilmDurationEqual0() throws ValidationException {
        filmController.create(film);
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageDuration, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление фильма c продолжительностью меньше 0")
    void updateFilmDurationLess0() throws ValidationException {
        filmController.create(film);
        film.setDuration(-20);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> filmController.update(film));
        Assertions.assertEquals(messageDuration, exception.getMessage());
    }
}
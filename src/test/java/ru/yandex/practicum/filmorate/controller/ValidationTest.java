package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class ValidationTest {

    private final Film film = new Film();
    private static final String MESSAGE_FILM_NAME = "Название фильма не может быть пустым!";
    private static final String MESSAGE_FILM_DESC = "Максимальная фильма длина описания — 200 символов!";
    private static final String MESSAGE_FILM_RELEASE_DATE = "Дата релиза — не раньше 28 декабря 1895 года!";
    private static final String MESSAGE_FILM_DURATION = "Продолжительность фильма должна быть положительной!";
    private final User user = new User();
    private static final String MESSAGE_USER_EMAIL = "Электронная почта не может быть пустой и должна содержать символ @!";
    private static final String MESSAGE_USER_LOGIN = "Логин не может быть пустым и содержать пробелы!";
    private static final String MESSAGE_USER_BIRTHDAY = "Дата рождения не может быть в будущем!";

    @BeforeEach
    void setUp() {
        film.setName("Titanic");
        film.setDescription("About ship Titanic");
        film.setDuration(180);
        film.setReleaseDate(LocalDate.of(1998, 1, 1));

        user.setName("Maria");
        user.setLogin("tochmary");
        user.setEmail("maria_toch@mail.ru");
        user.setBirthday(LocalDate.of(1986, 4, 28));
    }

    @Test
    @DisplayName("Проверка фильма")
    void createFilm() throws ValidationException {
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным именем")
    void createFilmNullName() {
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c пустым именем")
    void createFilmEmptyName() {
        film.setName("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_NAME, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным описанием")
    void createFilmDescNull() throws ValidationException {
        film.setDescription(null);
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c описанием равным 200 символов")
    void createFilmDesc200() throws ValidationException {
        String newDesc = "Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts.";
        film.setDescription(newDesc);
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c описанием больше 200 символов")
    void createFilmDescMore200() {
        film.setDescription("Titanic is a 1997 American epic romance and disaster film directed, written, produced, and " +
                "co-edited by James Cameron. Incorporating both historical and fictionalized aspects, " +
                "it is based on accounts of the sinking of the RMS Titanic");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DESC, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненным днем релиза")
    void createFilmReleaseDateNull() throws ValidationException {
        film.setReleaseDate(null);
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c днем релиза равным 28.12.1895")
    void createFilmReleaseDate28121895() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c днем релиза раньше 28.12.1895")
    void createFilmReleaseDateLess28121895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28).minusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_RELEASE_DATE, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c незаполненной продолжительностью")
    void createFilmDurationNull() throws ValidationException {
        film.setDuration(null);
        Validation.checkFilm(film);
    }

    @Test
    @DisplayName("Проверка фильма c продолжительностью равной 0")
    void createFilmDurationEqual0() {
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка фильма c продолжительностью меньше 0")
    void createFilmDurationLess0() {
        film.setDuration(-20);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkFilm(film));
        Assertions.assertEquals(MESSAGE_FILM_DURATION, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пустого пользователя")
    void createEmptyUser() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(new User()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя")
    void createUser() throws ValidationException {
        Validation.checkUser(user);
    }

    @Test
    @DisplayName("Проверка пользователя c незаполненным email")
    void createUserNullEmail() {
        user.setEmail(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c пустым email")
    void createUserEmptyEmail() {
        user.setEmail("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c email, не содержащим @ ")
    void createUserWithoutAtEmail() {
        user.setEmail("maria");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_EMAIL, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c незаполненным логином")
    void createUserNullLogin() {
        user.setLogin(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c пустым логином")
    void createUserEmptyLogin() {
        user.setLogin("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c логином, содержащим пробелы")
    void createUserWithSpaceLogin() {
        user.setLogin("toch mary");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_LOGIN, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c незаполненным днем рождения")
    void createUserNullBirthDay() throws ValidationException {
        user.setBirthday(null);
        System.out.println(user);
        Validation.checkUser(user);
    }

    @Test
    @DisplayName("Проверка пользователя c днем рождения в будущем")
    void createUserBirthDayFuture() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> Validation.checkUser(user));
        Assertions.assertEquals(MESSAGE_USER_BIRTHDAY, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка пользователя c незаполненным именем")
    void createUserNullName() throws ValidationException {
        user.setName(null);
        Validation.checkUser(user);
    }

}
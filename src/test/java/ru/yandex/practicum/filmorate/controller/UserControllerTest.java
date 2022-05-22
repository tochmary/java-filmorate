package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    private UserController userController;
    private final User user = new User();
    private static final String messageEmail = "Электронная почта не может быть пустой и должна содержать символ @!";
    private static final String messageLogin = "Логин не может быть пустым и содержать пробелы!";
    private static final String messageBirthDay = "Дата рождения не может быть в будущем!";

    @BeforeEach
    void setUp() {
        userController = new UserController();
        user.setName("Maria");
        user.setLogin("tochmary");
        user.setEmail("maria_toch@mail.ru");
        user.setBirthday(LocalDate.of(1986, 4, 28));
    }

    @Test
    @DisplayName("Пустой список пользователей")
    void findAllEmptyUsers() {
        Assertions.assertEquals(0, userController.findAll().size(),
                "Непустое количество пользователей!");
    }

    @Test
    @DisplayName("Непустой список пользователей")
    void findAllNotEmptyUsers() throws ValidationException {
        userController.create(user);
        Assertions.assertNotEquals(0, userController.findAll().size(),
                "Пустое количество пользователей!");
    }

    @Test
    @DisplayName("Создание пустого пользователя")
    void createEmptyUser() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(new User()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя")
    void createUser() throws ValidationException {
        userController.create(user);
        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("Maria", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertEquals(LocalDate.of(1986, 4, 28), user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }

    @Test
    @DisplayName("Создание пользователя c незаполненным email")
    void createUserNullEmail() {
        user.setEmail(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c пустым email")
    void createUserEmptyEmail() {
        user.setEmail("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c email, не содержащим @ ")
    void createUserWithoutAtEmail() {
        user.setEmail("maria");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c незаполненным логином")
    void createUserNullLogin() {
        user.setLogin(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c пустым логином")
    void createUserEmptyLogin() {
        user.setLogin("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c логином, содержащим пробелы")
    void createUserWithSpaceLogin() {
        user.setLogin("toch mary");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c незаполненным днем рождения")
    void createUserNullBirthDay() throws ValidationException {
        user.setBirthday(null);
        System.out.println(user);
        userController.create(user);

        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("Maria", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertNull(user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }

    @Test
    @DisplayName("Создание пользователя c днем рождения в будущем")
    void createUserBirthDayFuture() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(messageBirthDay, exception.getMessage());
    }

    @Test
    @DisplayName("Создание пользователя c незаполненным именем")
    void createUserNullName() throws ValidationException {
        user.setName(null);
        userController.create(user);
        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("tochmary", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertEquals(LocalDate.of(1986, 4, 28), user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser() throws ValidationException {
        userController.create(user);
        user.setName("Mariia");
        userController.update(user);
        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("Mariia", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertEquals(LocalDate.of(1986, 4, 28), user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }

    @Test
    @DisplayName("Обновление  пустого пользователя")
    void updateEmptyUser() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(new User()));
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c незаполненным email")
    void updateUserNullEmail() throws ValidationException {
        userController.create(user);
        user.setEmail(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c пустым email")
    void updateUserEmptyEmail() throws ValidationException {
        userController.create(user);
        user.setEmail("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c email, не содержащим @ ")
    void updateUserWithoutAtEmail() throws ValidationException {
        userController.create(user);
        user.setEmail("maria");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageEmail, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c незаполненным логином")
    void updateUserNullLogin() throws ValidationException {
        userController.create(user);
        user.setLogin(null);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c пустым логином")
    void updateUserEmptyLogin() throws ValidationException {
        userController.create(user);
        user.setLogin("");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c логином, содержащим пробелы")
    void updateUserWithSpaceLogin() throws ValidationException {
        userController.create(user);
        user.setLogin("toch mary");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageLogin, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c незаполненным днем рождения")
    void updateUserNullBirthDay() throws ValidationException {
        userController.create(user);
        user.setBirthday(null);
        System.out.println(user);
        userController.update(user) ;

        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("Maria", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertNull(user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }

    @Test
    @DisplayName("Обновление  пользователя c днем рождения в будущем")
    void updateUserBirthDayFuture() throws ValidationException {
        userController.create(user);
        user.setBirthday(LocalDate.now().plusMonths(1));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> userController.update(user) );
        Assertions.assertEquals(messageBirthDay, exception.getMessage());
    }

    @Test
    @DisplayName("Обновление  пользователя c незаполненным именем")
    void updateUserNullName() throws ValidationException {
        userController.create(user);
        user.setName(null);
        userController.update(user) ;
        User user1 = userController.findAll().get(0);
        Assertions.assertEquals("tochmary", user1.getName(),
                "Некорректное имя пользователя");
        Assertions.assertEquals("tochmary", user1.getLogin(),
                "Некорректное логин пользователя");
        Assertions.assertEquals("maria_toch@mail.ru", user1.getEmail(),
                "Некорректный email пользователя");
        Assertions.assertEquals(LocalDate.of(1986, 4, 28), user1.getBirthday(),
                "Некорректный день рождения пользователя");
    }
}
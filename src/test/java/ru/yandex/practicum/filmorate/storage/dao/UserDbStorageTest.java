package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

/*
ТЕСТЫ ВЫПОЛНЯЮТСЯ ПОСЛЕДОВАТЕЛЬНО
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(SAME_THREAD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final static String NAME = "Maria";
    private final static String LOGIN = "tochmary";
    private final static String EMAIL = "maria_toch@mail.ru";
    private final static LocalDate BIRTHDAY = LocalDate.of(1986, 4, 28);

    @Test
    @Order(1)
    @DisplayName("Создание пользователя")
    void create() {
        User user = userDbStorage.create(new User(null, EMAIL, LOGIN, NAME, BIRTHDAY, null));

        Assertions.assertNotNull(user.getId(), "Не создан пользователь!");
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(2)
    @DisplayName("Создание второго пользователя")
    void createSecond() {
        User user = userDbStorage.create(new User(null,
                "Second" + EMAIL, "Second" + LOGIN, "Second" + NAME, BIRTHDAY.plusMonths(1), null));

        Assertions.assertNotNull(user.getId(), "Не создан пользователь!");
        Assertions.assertEquals(2, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("Second" + NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals("Second" + LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals("Second" + EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY.plusMonths(1), user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(3)
    @DisplayName("Получение пользователя по id")
    void findById() {
        Optional<User> userOptional = userDbStorage.findById(1);

        Assertions.assertTrue(userOptional.isPresent(), "Не найден пользователь!");
        User user = userOptional.get();
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(4)
    @DisplayName("Получение списка пользователей")
    void findAll() {
        List<User> users = userDbStorage.findAll();

        Assertions.assertFalse(users.isEmpty(), "Список пользователей пуст!");
        Assertions.assertEquals(2, users.size(), "Некорректное общее количество пользователей!");
        User user = users.get(0);
        Assertions.assertEquals(1, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(NAME, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(LOGIN, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(EMAIL, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(BIRTHDAY, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }


    @Test
    @Order(5)
    @DisplayName("Существование пользователя")
    void isUserExist() {
        boolean isUserExist = userDbStorage.isUserExist(2);
        Assertions.assertTrue(isUserExist, "Пользователь не существует!");
    }

    @Test
    @Order(6)
    @DisplayName("Не существования пользователя")
    void isUserNotExist() {
        boolean isUserExist = userDbStorage.isUserExist(3);
        Assertions.assertFalse(isUserExist, "Пользователь существует!");
    }

    @Test
    @Order(7)
    @DisplayName("Обновление пользователя")
    void update() {
        String nameNew = "Marisa";
        String loginNew = "mary";
        String emailNew = "maria@mail.ru";
        LocalDate birthdayNew = LocalDate.of(1989, 4, 28);
        User user = userDbStorage.update(new User(2, emailNew, loginNew, nameNew, birthdayNew, null));

        Assertions.assertEquals(2, user.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(nameNew, user.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(loginNew, user.getLogin(), "Некорректное значение поле LOGIN!");
        Assertions.assertEquals(emailNew, user.getEmail(), "Некорректное значение поле EMAIL!");
        Assertions.assertEquals(birthdayNew, user.getBirthday(), "Некорректное значение поле BIRTHDAY!");
    }

    @Test
    @Order(8)
    @DisplayName("Добавление друга пользователю")
    void addFriend() {
        userDbStorage.addFriend(1, 2);

        Set<Integer> friendIds = userDbStorage.getFriendsById(1);
        Assertions.assertFalse(friendIds.isEmpty(), "Список друзей пуст!");
        Assertions.assertEquals(1, friendIds.size(), "Некорректное общее количество друзей!");

        Boolean isConfirmed = userDbStorage.getStatusFriends(1, 2);
        Assertions.assertFalse(isConfirmed, "Дружба подтверждена!");

        Set<Integer> friendId2s = userDbStorage.getFriendsById(2);
        Assertions.assertTrue(friendId2s.isEmpty(), "Список друзей не пуст!");
    }

    @Test
    @Order(9)
    @DisplayName("Подтверждение дружбы")
    void confirmFriend() {
        userDbStorage.confirmFriend(1, 2);
        Boolean isConfirmed = userDbStorage.getStatusFriends(1, 2);
        Assertions.assertTrue(isConfirmed, "Дружба не подтверждена!");
    }

    @Test
    @Order(10)
    @DisplayName("Удаление дружбы")
    void deleteFriend() {
        userDbStorage.deleteFriend(1, 2);
        Set<Integer> friendIds = userDbStorage.getFriendsById(1);
        Assertions.assertTrue(friendIds.isEmpty(), "Список друзей не пуст!");
    }
}
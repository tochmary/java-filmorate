package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public static void checkUser(User user) {
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @!");
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.findAll();
    }

    public User getUserById(Integer id) {
        return userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователя с id = " + id + " не существует!")
        );
    }

    public User create(User user) {
        checkUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUser(user);
        checkUserForExist(user.getId());
        return userStorage.update(user);
    }

    public User addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (!user.getFriends().contains(friendId)) {
            userStorage.addFriend(id, friendId);
        }
        if (friend.getFriends().contains(id)) {
            userStorage.confirmFriend(id, friendId);
            userStorage.confirmFriend(friendId, id);
        }
        return user;
    }

    public User deleteFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        if (user.getFriends().contains(friendId)) {
            userStorage.deleteFriend(id, friendId);
            userStorage.deleteFriend(friendId, id);
        }
        return user;
    }

    public List<User> getFriends(Integer id) {
        User user = getUserById(id);
        return user.getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user1 = getUserById(id);
        User user2 = getUserById(otherId);
        return user1.getFriends()
                .stream()
                .filter(user2.getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public void checkUserForExist(Integer id) {
        if (!userStorage.isUserExist(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует!");
        }
    }
}

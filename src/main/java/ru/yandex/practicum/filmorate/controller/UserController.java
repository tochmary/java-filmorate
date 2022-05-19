package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    //GET /users — для получения списка пользователей.
    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    //POST /users — для добавления нового пользователя в список.
    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        checkUser(user);
        int idUser = generateId();
        user.setId(idUser);
        users.put(idUser, user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    //PUT /users — обновление значения полей существующего.
    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        Integer idUser = user.getId();
        checkUser(user);
        if (isExistUser(user)) {
            users.put(idUser, user);
            log.debug("Обновлен пользователь: {}", user);
        } else {
            throw new ValidationException("Пользователя с id = " + idUser + " не существует!");
        }
        return user;
    }

    private int generateId() {
        return id++;
    }

    private boolean isExistUser(User user) {
        return users.containsKey(user.getId());
    }

    private void checkUser(User user) throws ValidationException {
        //электронная почта не может быть пустой и должна содержать символ @;
        //логин не может быть пустым и содержать пробелы;
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        //дата рождения не может быть в будущем.
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
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }
}

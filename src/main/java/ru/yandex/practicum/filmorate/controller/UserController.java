package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
        Validation.checkUser(user);
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
        Validation.checkUser(user);
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
}

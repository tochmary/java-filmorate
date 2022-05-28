package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    public User create(User user) {
        int idUser = generateId();
        user.setId(idUser);
        users.put(idUser, user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    public void addFriend(User user, Integer friendId) {
        user.addFriend(friendId);
        log.debug("Добавлен для пользователя c id {} друг с id {}", id, friendId);
    }

    public void deleteFriend(User user, Integer friendId) {
        user.deleteFriend(friendId);
        log.debug("Удален для пользователя c id {} друг с id {}", id, friendId);
    }

    public boolean isExistUser(Integer id) {
        return users.containsKey(id);
    }

    private int generateId() {
        return id++;
    }
}

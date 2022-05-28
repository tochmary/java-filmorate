package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    Optional<User> findById(Integer id);

    User create(User user);

    User update(User user);

    User addFriend(User user, Integer friendId);

    User deleteFriend(User user, Integer friendId);

    boolean isExistUser(Integer id);
}

package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        // выполняем запрос к базе данных.
        String sql = "SELECT * FROM PUBLIC.USERS ORDER BY ID";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        users.forEach(u -> u.setFriends(getFriendsById(u.getId())));
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) {
        // выполняем запрос к базе данных.
        String sql = "SELECT * FROM PUBLIC.USERS WHERE ID = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);

        // обрабатываем результат выполнения запроса
        if (users.isEmpty()) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        } else {
            User user = users.get(0);
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            user.setFriends(getFriendsById(user.getId()));
            return Optional.of(user);
        }
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)\n" +
                "VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE PUBLIC.USERS " +
                "SET EMAIL = ? ," +
                "LOGIN = ? ," +
                "NAME = ? , " +
                "BIRTHDAY = ? " +
                "WHERE id = ? ;";
        jdbcTemplate.update(sql,
                user.getEmail(),user.getLogin(),user.getName(),user.getBirthday(),user.getId());
        user.setFriends(getFriendsById(user.getId()));
        return user;
    }

    @Override
    public void addFriend(User user, Integer friendId) {
        String sql = "INSERT INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID)\n" +
                "VALUES (?, ?);";
        jdbcTemplate.update(sql,user.getId(),friendId);
        user.setFriends(getFriendsById(user.getId()));
    }

    @Override
    public void deleteFriend(User user, Integer friendId) {
        String sql = "DELETE FROM PUBLIC.FRIENDS " +
                "WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql,user.getId(),friendId);
        user.setFriends(getFriendsById(user.getId()));
    }

    public Set<Integer> getFriendsById(Integer id) {
        String sql = "SELECT FRIEND_ID FROM PUBLIC.FRIENDS WHERE USER_ID = ? ";
        List<Integer> friends = jdbcTemplate.query(sql, (rs, rowNum) -> getFriendId(rs), id);
        return friends.stream().collect(Collectors.toSet());
    }

    @Override
    public boolean isUserExist(Integer id) {
        return findById(id).isPresent();
    }

    private User makeUser(ResultSet rs) throws SQLException {
        // реализуйте маппинг результата запроса в объект класса Follow
        return new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                null
        );
    }

    private Integer getFriendId(ResultSet rs) throws SQLException {
       return rs.getInt("friend_id");
    }

}

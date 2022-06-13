package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        // выполняем запрос к базе данных.
        String sql = "SELECT * FROM PUBLIC.FILMS ORDER BY ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach(f -> f.setLike(getLikesById(f.getId())));
        return films;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        // выполняем запрос к базе данных.
        String sql = "SELECT * FROM PUBLIC.FILMS WHERE ID = ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);

        // обрабатываем результат выполнения запроса
        if (films.isEmpty()) {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        } else {
            Film film = films.get(0);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            film.setLike(getLikesById(film.getId()));
            return Optional.of(film);
        }
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION)\n" +
                "VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE PUBLIC.FILMS " +
                "SET NAME = ? ," +
                "DESCRIPTION = ? ," +
                "RELEASE_DATE = ? , " +
                "DURATION = ? " +
                "WHERE id = ? ;";
        jdbcTemplate.update(sql,
                film.getName(),film.getDescription(),film.getReleaseDate(),film.getDuration(),film.getId());
        film.setLike(getLikesById(film.getId()));
        return film;
    }

    @Override
    public void addLike(Film film, Integer userId) {
        String sql = "INSERT INTO PUBLIC.LIKES (FILM_ID, USER_ID)\n" +
                "VALUES (?, ?);";
        jdbcTemplate.update(sql,film.getId(),userId);
        film.setLike(getLikesById(film.getId()));
    }

    @Override
    public void deleteLike(Film film, Integer userId) {
        String sql = "DELETE FROM PUBLIC.LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sql,film.getId(),userId);
        film.setLike(getLikesById(film.getId()));
    }

    public Set<Integer> getLikesById(Integer id) {
        String sql = "SELECT USER_ID FROM PUBLIC.LIKES WHERE FILM_ID = ?";
        List<Integer> likes = jdbcTemplate.query(sql, (rs, rowNum) -> getLikeUserId(rs), id);
        return likes.stream().collect(Collectors.toSet());
    }

    @Override
    public boolean isFilmExist(Integer id) {
        return findById(id).isPresent();
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        // реализуйте маппинг результата запроса в объект класса Follow
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                null
        );
    }

    private Integer getLikeUserId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }

}

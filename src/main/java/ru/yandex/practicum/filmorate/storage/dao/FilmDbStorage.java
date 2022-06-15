package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.HashSet;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         MpaDbStorage mpaDbStorage,
                         GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> findAll() {
        // выполняем запрос к базе данных.
        String sql = "SELECT * FROM PUBLIC.FILMS ORDER BY ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach(f -> {
            f.setLike(getLikesById(f.getId()));
            f.setGenres(getGenresByFilmId(f.getId()));
        });
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
            film.setGenres(getGenresByFilmId(film.getId()));
            return Optional.of(film);
        }
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa() == null ? null : film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().forEach(g -> addGenre(film.getId(), g.getId()));
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE PUBLIC.FILMS " +
                "SET NAME = ? ," +
                "DESCRIPTION = ? ," +
                "RELEASE_DATE = ? , " +
                "DURATION = ? ," +
                "MPA_ID = ? " +
                "WHERE id = ? ;";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        film.setMpa(mpaDbStorage.findById(film.getMpa().getId()).orElse(null));
        if (film.getGenres() != null) {
            deleteGenres(film.getId());
            if (!film.getGenres().isEmpty()) {
                film.getGenres().forEach(g -> addGenre(film.getId(), g.getId()));
                film.setGenres(getGenresByFilmId(film.getId()));
            }
        }
        film.setLike(getLikesById(film.getId()));
        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO PUBLIC.LIKES (FILM_ID, USER_ID) VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
        findById(filmId).get().setLike(getLikesById(filmId));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM PUBLIC.LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sql, filmId, userId);
        findById(filmId).get().setLike(getLikesById(filmId));
    }

    public Set<Integer> getLikesById(Integer id) {
        String sql = "SELECT USER_ID FROM PUBLIC.LIKES WHERE FILM_ID = ?";
        List<Integer> likes = jdbcTemplate.query(sql, (rs, rowNum) -> getLikeUserId(rs), id);
        return new HashSet<>(likes);
    }

    @Override
    public boolean isFilmExist(Integer id) {
        return findById(id).isPresent();
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer mpa_id = rs.getInt("mpa_id");
        Mpa mpa = mpaDbStorage.findById(mpa_id).orElse(new Mpa());
        // реализуйте маппинг результата запроса в объект класса Follow
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                null,
                null
        );
    }

    private Integer getLikeUserId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }

    private Integer getGenreId(ResultSet rs) throws SQLException {
        return rs.getInt("genre_id");
    }

    private Set<Genre> getGenresByFilmId(Integer film_id) {
        String sql = "SELECT GENRE_ID FROM PUBLIC.FILM_GENRES WHERE FILM_ID = ? ORDER BY GENRE_ID;";
        List<Integer> genreIds = jdbcTemplate.query(sql, (rs, rowNum) -> getGenreId(rs), film_id);
        if (genreIds.isEmpty()) {
            return null;
        } else {
            return genreIds.stream().map(id -> genreDbStorage.findById(id)
                    .orElse(new Genre())).collect(Collectors.toSet());
        }
    }

    private void addGenre(Integer film_id, Integer genre_id) {
        String sql = "INSERT INTO PUBLIC.FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.update(sql, film_id, genre_id);
    }

    private void deleteGenres(Integer film_id) {
        String sql = "DELETE FROM PUBLIC.FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film_id);
    }

}

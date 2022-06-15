package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

/*
ТЕСТЫ ВЫПОЛНЯЮТСЯ ПОСЛЕДОВАТЕЛЬНО
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(SAME_THREAD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final String name = "Titanic";
    private final String description = "About ship Titanic";
    private final Integer duration = 180;
    private final LocalDate releaseDate = LocalDate.of(1998, 1, 1);
    private final Mpa mpa = new Mpa(1, null, null);
    private final Set<Genre> genres = Set.of(new Genre(1, null), new Genre(2, null));
    private static Integer userId;

    @Test
    @Order(1)
    @DisplayName("Создание фильма")
    void create() {
        Film film = filmDbStorage.create(new Film(null,
                name, description, releaseDate, duration, mpa, genres, null));

        Assertions.assertNotNull(film.getId(), "Не создан фильм!");
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(name, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(description, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(releaseDate, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(duration, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(mpa.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(genres.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(genres.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(2)
    @DisplayName("Создание второго фильма")
    void createSecond() {
        Film film = filmDbStorage.create(new Film(null,
                name + "2", description + "2", releaseDate.plusMonths(1), duration + 2, null, null, null));

        Assertions.assertNotNull(film.getId(), "Не создан фильм!");
        Assertions.assertEquals(2, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(name + "2", film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(description + "2", film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(releaseDate.plusMonths(1), film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(duration + 2, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertNull(film.getMpa(), "Некорректное значение поле MPA!");
        Assertions.assertNull(film.getGenres(), "Некорректное значение поле GENRES!");
    }

    @Test
    @Order(3)
    @DisplayName("Получение фильма по id")
    void findById() {
        Optional<Film> filmOptional = filmDbStorage.findById(1);

        Assertions.assertTrue(filmOptional.isPresent(), "Не найден фильм!");
        Film film = filmOptional.get();
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(name, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(description, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(releaseDate, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(duration, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(mpa.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(genres.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(genres.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(4)
    @DisplayName("Получение списка фильмов")
    void findAll() {
        List<Film> films = filmDbStorage.findAll();

        Assertions.assertFalse(films.isEmpty(), "Список фильмов пуст!");
        Assertions.assertEquals(2, films.size(), "Некорректное общее количество фильмов!");
        Film film = films.get(0);
        Assertions.assertEquals(1, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(name, film.getName(), "Некорректное значение поле NAME!");
        Film film2 = films.get(1);
        Assertions.assertEquals(2, film2.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(name + "2", film2.getName(), "Некорректное значение поле NAME!");

    }

    @Test
    @Order(5)
    @DisplayName("Существование фильма")
    void isFilmExist() {
        boolean isFilmExist = filmDbStorage.isFilmExist(2);
        Assertions.assertTrue(isFilmExist, "Фильм не существует!");
    }

    @Test
    @Order(6)
    @DisplayName("Не существования фильма")
    void isFilmNotExist() {
        boolean isFilmExist = filmDbStorage.isFilmExist(3);
        Assertions.assertFalse(isFilmExist, "Фильм существует!");
    }

    @Test
    @Order(7)
    @DisplayName("Обновление фильма")
    void update() {
        String nameNew = "Titanic";
        String descriptionNew = "About ship Titanic";
        Integer durationNew = 180;
        LocalDate releaseDateNew = LocalDate.of(1998, 1, 1);
        Mpa mpaNew = new Mpa(1, null, null);
        Set<Genre> genresNew = Set.of(new Genre(2, null));

        Film film = filmDbStorage.update(new Film(2,
                nameNew, descriptionNew, releaseDateNew, durationNew, mpaNew, genresNew, null));

        Assertions.assertEquals(2, film.getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals(nameNew, film.getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals(descriptionNew, film.getDescription(), "Некорректное значение поле DESCRIPTION!");
        Assertions.assertEquals(releaseDateNew, film.getReleaseDate(), "Некорректное значение поле RELEASE_DATE!");
        Assertions.assertEquals(durationNew, film.getDuration(), "Некорректное значение поле DURATION!");
        Assertions.assertEquals(mpaNew.getId(), film.getMpa().getId(), "Некорректное значение поле MPA!");
        Assertions.assertEquals(genresNew.size(), film.getGenres().size(), "Некорректное количество жанров!!");
        Assertions.assertEquals(genresNew.stream().map(Genre::getId).collect(Collectors.toSet()),
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()),
                "Некорректное id жанров!!");
    }

    @Test
    @Order(8)
    @DisplayName("Добавление лайка фильма")
    void addLike() {
        User user = userDbStorage.create(new User(null,
                "maria_toch@mail.ru", "tochmary", "Maria", LocalDate.of(1986, 4, 28), null));
        userId = user.getId();
        filmDbStorage.addLike(1, userId);

        Set<Integer> userIds = filmDbStorage.getLikesById(1);
        Assertions.assertFalse(userIds.isEmpty(), "Список лайков пуст!");
        Assertions.assertEquals(1, userIds.size(), "Некорректное общее количество лайков!");

    }

    @Test
    @Order(9)
    @DisplayName("Удаление лайка фильма")
    void deleteLike() {
        filmDbStorage.deleteLike(1, userId);
        Set<Integer> userIds = filmDbStorage.getLikesById(1);
        Assertions.assertTrue(userIds.isEmpty(), "Список лайков не пуст!");
    }
}
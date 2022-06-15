package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    @DisplayName("Получение списка справочника MPA")
    void findAll() {
        List<Mpa> mpaList = mpaDbStorage.findAll();

        Assertions.assertFalse(mpaList.isEmpty(), "Справочник MPA пуст!");
        Assertions.assertEquals(5, mpaList.size(), "Некорректное общее количество строк справочника MPA!");
        Assertions.assertEquals(5, mpaList.get(4).getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("NC-17", mpaList.get(4).getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals("лицам до 18 лет просмотр запрещён", mpaList.get(4).getDescription(), "Некорректное значение поле DESCRIPTION!");

    }

    @Test
    @DisplayName("Получение рейтинга MPA по id")
    void findById() {
        Optional<Mpa> mpa = mpaDbStorage.findById(1);

        Assertions.assertTrue(mpa.isPresent(), "Не найдена строка в справочнике MPA!");
        Assertions.assertEquals(1, mpa.get().getId(), "Некорректное значение поле ID!");
        Assertions.assertEquals("G", mpa.get().getName(), "Некорректное значение поле NAME!");
        Assertions.assertEquals("у фильма нет возрастных ограничений", mpa.get().getDescription(), "Некорректное значение поле DESCRIPTION!");
    }
}
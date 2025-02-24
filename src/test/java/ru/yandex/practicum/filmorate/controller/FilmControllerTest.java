package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FilmControllerTest {
    private static final LocalDate CORRECT_RELEASE = LocalDate.of(1895, 12, 28);
    private static final LocalDate INCORRECT_RELEASE = LocalDate.of(1895, 12, 27);
    private static final int CORRECT_DURATION = 120;
    private static final int INCORRECT_DURATION = -1;

    FilmController controller = new FilmController();

    private Film testFilm;

    @BeforeEach
    public void createTestFilm() {
        testFilm = Film.builder()
                .name("kin-dza-dza")
                .description("comedy")
                .releaseDate(CORRECT_RELEASE)
                .duration(CORRECT_DURATION)
                .build();
    }

    @DisplayName(value = "Создание фильма - Ошибка:пустое название")
    @Test
    public void createFilmWithNoNameTest() {
        testFilm.setName("");
        try {
            controller.createFilm(testFilm);
        } catch (Exception e) {
            assertEquals("Название фильма не может быть пустым", e.getMessage());
        }
    }

    @DisplayName(value = "Создание фильма - Ошибка:некорректная дата релиза")
    @Test
    public void createFilmWithIncorrectReleaseTest() {
        testFilm.setReleaseDate(INCORRECT_RELEASE);
        try {
            controller.createFilm(testFilm);
        } catch (Exception e) {
            assertEquals("Некорректная дата релиза", e.getMessage());
        }
    }

    @DisplayName(value = "Создание фильма - Ошибка:отрицательная продолжительность")
    @Test
    public void createFilmWithNegativeDurationTest() {
        testFilm.setDuration(INCORRECT_DURATION);
        try {
            controller.createFilm(testFilm);
        } catch (Exception e) {
            assertEquals("Продолжительность не может быть отрицательной", e.getMessage());
        }
    }

    @DisplayName(value = "Создание фильма - Ошибка:описание длиннее 200 символов")
    @Test
    public void createFilmWithTooLongDescriptionTest() {
        testFilm.setDescription("fhghfjfjfghfhfjdjdkdjkfjfhfhghghhfjfjdjdjdhfhfhfhfjdjdjfhfhfjdjdhfhfdjdjfdhfjdjdh" +
                "fhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhfhdjdjkksksldkdjfhffhdhdjdjdkdkddkfjfjfhhryrufkkfkf" +
                "fjfjfjfjfjfjfjfjfjfhdhdgbddhfhfytuttikkhkhkhkhkhkgklglglflf;f;;ff;;f;f;flfkfjkfjfffhhgygygydfhufh" +
                "fjfffhufhufhufhfuhfuhfuhfufhufhufhuyryryryryryyryrurrirroorororororofoflflfkdjdjhdhfjhjfkdlddkdkd" +
                "djdjdjdjdjdjdjdhdhdhdgdgdhdhdjdjdkdkdkdkdldldld;d;d;d;;d;d;d;fkfhjdhdhhdgdgdfdgfhjfjgkgkgkgkglgg" +
                "ghfjfjkdkldldldhfjfdkjdklвпррыоыоыоырврпапапапапапапапвававававававвавав" + "кажется хватит, лол!");
        try {
            controller.createFilm(testFilm);
        } catch (Exception e) {
            assertEquals("Слишком длинное описание фильма", e.getMessage());
        }
    }
}

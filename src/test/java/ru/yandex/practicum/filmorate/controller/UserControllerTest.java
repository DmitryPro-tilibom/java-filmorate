package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {
    private User testUser;
    UserController controller = new UserController();
    private static final LocalDate CORRECTBIRTHDAY = LocalDate.of(1990, 01, 02);
    private static final LocalDate INCORRECTBIRTHDAY = LocalDate.of(2030,01, 02);

    @BeforeEach
    public void createTestUser() {
        testUser = User.builder()
                .email("my@mail.ru")
                .login("tilibom")
                .name("Dimon")
                .birthday(CORRECTBIRTHDAY)
                .build();
    }

    @Test
    public void createCorrectUserTest() {
        User createdUser = controller.createUser(testUser);
        assertTrue(createdUser.getId() != 0);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Неверный email")
    @Test
    public void createUserWithIncorrectEmailTest() {
        testUser.setEmail("fhgjfk");
        try {
            controller.createUser(testUser);
        } catch (Exception e) {
            assertEquals("Введенное значение не является адресом электронной почты.", e.getMessage());
        }
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Дата рождения не в прошлом")
    @Test
    public void createUserWithIncorrectBirthday() {
        testUser.setBirthday(INCORRECTBIRTHDAY);
        try {
            controller.createUser(testUser);
        } catch (Exception e) {
            assertEquals("Дата рождения должна быть в прошлом", e.getMessage());
        }
    }

    @Test
    public void loginIsUsedAsNameIfNameIsEmptyTest() {
        testUser.setName("");
        controller.createUser(testUser);
        assertEquals(testUser.getName(), testUser.getLogin());
    }
}

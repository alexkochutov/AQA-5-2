package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input[name='login']").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2.heading").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input[name='login']").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input[name='password']").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id='error-notification'] .notification__title").shouldHave(exactText("Ошибка"));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input[name='login']").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input[name='password']").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id='error-notification'] .notification__title").shouldHave(exactText("Ошибка"));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input[name='login']").setValue(wrongLogin);
        $("[data-test-id='password'] input[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id='error-notification'] .notification__title").shouldHave(exactText("Ошибка"));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input[name='login']").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input[name='password']").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification']").shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id='error-notification'] .notification__title").shouldHave(exactText("Ошибка"));
        $("[data-test-id='error-notification'] .notification__content").shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;
public class LoginTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldBeSuccessful(){
        var registerUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registerUser.getLogin());
        $("[data-test-id='password'] input").setValue(registerUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("h2").shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }
    @Test
    void notRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
        void blockedUser()  {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }
    @Test
    void incorrectLogin(){
        var registerUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registerUser.getPassword());
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }
    @Test
    void incorrectPassword(){
        var registerUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registerUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login'] .button__content").click();
        $("[data-test-id='error-notification']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }



}


package ru.netology.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.UserInfo;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.UserInfo.*;

public class CardsTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = UserInfo.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = UserInfo.getVerificationCode();
        dashboardPage = verificationPage.verify(verificationCode);
    }

    @Test
    public void shouldTransferRandomFrom1To2() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualBalanceFirstCard);
        assertEquals(expectedSecondCardBalance, actualBalanceSecondCard);
    }

    @Test
    public void shouldTransferRandomFrom2To1() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = generateValidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualBalanceFirstCard);
        assertEquals(expectedSecondCardBalance, actualBalanceSecondCard);
    }

    @Test
    public void shouldTransferMaxFrom1To2() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = firstCardBalance;
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualBalanceFirstCard);
        assertEquals(expectedSecondCardBalance, actualBalanceSecondCard);
    }

    @Test
    public void shouldTransferMaxFrom2To1() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = secondCardBalance;
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualBalanceFirstCard);
        assertEquals(expectedSecondCardBalance, actualBalanceSecondCard);
    }

    @Test //issue
    public void shouldReceiveErrorIfOverMaxAmountFrom1To2() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = generateInvalidAmount(firstCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        transferPage.makeTransfer(String.valueOf(amount), secondCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }

    @Test //issue
    public void shouldReceiveErrorIfOverMaxAmountFrom2To1() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }

    @Test //issue
    public void shouldReceiveErrorTransferRandomFrom1To1() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 10;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
    }

    @Test //issue
    public void shouldReceiveErrorTransferRandomFrom2To2() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 10;
        var transferPage = dashboardPage.selectCardToTransfer(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), secondCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
    }

    @Test// issue
    public void shouldReceiveErrorTransfer0From1To2() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 0;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(String.valueOf(amount), secondCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
    }

    @Test// issue
    public void shouldReceiveErrorTransfer0From2To1() {
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = 0;
        var transferPage = dashboardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(String.valueOf(amount), secondCard);
        transferPage.findErrorMessage("ошибка");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
    }
}


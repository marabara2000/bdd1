package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @Test
    void shouldTransferFromFirstToSecond() {
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode(info);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        Selenide.open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(info);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);



        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertAll(() ->assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard));
    }

    @Test
    void shouldGetErrorMessageAmountMoreBalance(){
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode(info);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        Selenide.open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(info);
        dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);

        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        assertAll(() -> transferPage.findErrorMessage("Ошибка! "),
                () -> dashboardPage.reloadDashboardPage(),
                () -> assertEquals(firstCardBalance, dashboardPage.getCardBalance(firstCardInfo)),
                () -> assertEquals(secondCardBalance, dashboardPage.getCardBalance(secondCardInfo))
        );
    }
}

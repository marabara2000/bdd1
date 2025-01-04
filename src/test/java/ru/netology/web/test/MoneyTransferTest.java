package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {

    @Test
    void  shouldTransferMoneyBetweenCards() {
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCode(info);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        Selenide.open("http://localhost:9999");
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(info);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        dashboardPage.selectCardToTransfer(firstCardInfo);

    }
}

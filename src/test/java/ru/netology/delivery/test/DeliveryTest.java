package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class DeliveryTest {


    @BeforeEach
    void setup() {

        open("http://localhost:9999");
    }

    @Test
    void name() {
        Faker faker = new Faker(new Locale("ru"));
        System.out.println( faker.name().fullName());
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе

        $("span[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("span[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("span[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        $("span[data-test-id='date'] .input__control").sendKeys(firstMeetingDate);

        $("span[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("span[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $("label[data-test-id='agreement']").click();
        $x("//button //span[contains(text(), 'Запланировать')]/../..").click();
        //$x("//button//span[contains(text(), 'Забронировать')]//..//span[contains(@class, 'spin_visible')]")
        //        .should(Condition.exist);
        $(".notification__title").should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);

        // перепланирование встречи
        $("span[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("span[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        $("span[data-test-id='date'] .input__control").sendKeys(secondMeetingDate);


        $x("//button //span[contains(text(), 'Запланировать')]/../..").click();
        $x("//*[contains(@class, 'notification__content') and contains(text(), 'У вас уже запланирована встреча на другую дату. Перепланировать?')]")
                .shouldBe(Condition.visible);
        $x("//button //span[contains(text(), 'Перепланировать')]/../..").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}

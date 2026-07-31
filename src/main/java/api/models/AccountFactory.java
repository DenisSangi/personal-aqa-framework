package api.models;

import utils.RandomDataGenerator;

import java.time.LocalDate;

import static api.models.Countries.getRandomCountryValue;

public class AccountFactory {

    public static AccountModel createValidAccountModel() {
        final LocalDate VALID_DATE_FROM_TODAY = RandomDataGenerator.generateDateFromTodayOfYearsAgo(RandomDataGenerator.generateNumberInRange(18, 90));

        return AccountModel.builder()
                .name(RandomDataGenerator.generateAlphanumeric(10))
                .email(RandomDataGenerator.generateEmail())
                .password(RandomDataGenerator.generateAlphanumeric(10))
                .birthDate(String.valueOf(VALID_DATE_FROM_TODAY.getDayOfMonth()))
                .birthMonth((String.valueOf(VALID_DATE_FROM_TODAY.getMonthValue())))
                .birthYear((String.valueOf(VALID_DATE_FROM_TODAY.getYear())))
                .firstName(RandomDataGenerator.generateAlphabetic(10))
                .lastName(RandomDataGenerator.generateAlphabetic(10))
                .address1(RandomDataGenerator.generateAlphabetic(10) + " " + RandomDataGenerator.generateNumberInRange(1, 100))
                .country(getRandomCountryValue())
                .zipCode(RandomDataGenerator.generateAlphabetic(10))
                .state(RandomDataGenerator.generateAlphabetic(10))
                .city(RandomDataGenerator.generateAlphabetic(10))
                .mobilePhone(RandomDataGenerator.generateNumber(10))
                .build();
    }
}

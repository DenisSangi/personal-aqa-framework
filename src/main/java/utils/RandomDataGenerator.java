package utils;


import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDataGenerator {

    public static String generateAlphabetic(int count) {
        char[] resultString = new char[count];

        for (int i = 0; i < count; i++) {
            resultString[i] = ((char) ('a' + ThreadLocalRandom.current().nextInt(26)));
        }

        return new String(resultString);
    }

    public static String generateNumber(int count) {
        char[] resultString = new char[count];

        for (int i = 0; i < count; i++) {
            resultString[i] = ((char) ('0' + ThreadLocalRandom.current().nextInt(10)));
        }

        return new String(resultString);
    }

    public static String generateAlphanumeric(int count) {
        StringBuilder resultString = new StringBuilder();

        for (int i = 0; i < count; i++) {
            boolean letterOrDigit = ThreadLocalRandom.current().nextBoolean();
            String result = letterOrDigit ? (generateAlphabetic(1)) : generateNumber(1);
            resultString.append(result);
        }

        return resultString.toString();
    }

    public static String generateEmail() {
        return generateAlphanumeric(10) + '@' + Domains.getRandomDomain();
    }

    public static LocalDate generateDateFromTodayOfYearsAgo(int yearsAgo) {
        LocalDate today = LocalDate.now();
        LocalDate exactYear = today.minusYears(yearsAgo);
        return exactYear.minusDays(ThreadLocalRandom.current().nextInt(365));
    }

    public static int generateNumberInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}

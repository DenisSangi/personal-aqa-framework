package utils;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class RandomDataGeneratorTest {

    @Test
    public void testGenerateAlphabetic() {

        String testingRandomAlphabetic = RandomDataGenerator.generateAlphabetic(10);
        System.out.println(testingRandomAlphabetic);
        String regex = "\\p{L}+$";
        assertEquals(testingRandomAlphabetic.length(), 10);
        assertTrue(testingRandomAlphabetic.matches(regex));

    }

    @Test
    public void testGenerateAlphanumeric() {

        String testingRandomAlphanumeric = RandomDataGenerator.generateAlphanumeric(19);
        System.out.println(testingRandomAlphanumeric);
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$";
        assertEquals(testingRandomAlphanumeric.length(), 19);
        assertTrue(testingRandomAlphanumeric.matches(regex));

    }

    @Test
    public void testGenerateNumber() {
        String testingNumber = RandomDataGenerator.generateNumber(100);
        System.out.println(testingNumber);
        assertEquals(testingNumber.length(), 100);
    }

    @Test
    public void testGenerateEmail() {
        String testEmail = RandomDataGenerator.generateEmail();
        System.out.println(testEmail);
        assertTrue(testEmail.contains("@"));
    }
}
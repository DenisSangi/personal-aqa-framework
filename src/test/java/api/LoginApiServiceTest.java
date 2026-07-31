package api;

import config.FrameworkConfig;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LoginApiServiceTest {

    private final LoginApiService loginApiService = new LoginApiService();

    @Test
    public void baseLoginApiTest() {
        assertTrue(loginApiService.verifyLogin(FrameworkConfig.APP_USER_EMAIL, FrameworkConfig.APP_USER_PASSWORD));
    }

    @Test
    public void incorrectCredentialsTest() {
        String incorrectEmail = "Qwedwe2@gmail.com";
        assertFalse(loginApiService.verifyLogin(incorrectEmail, FrameworkConfig.APP_USER_PASSWORD));
        String incorrectPassword = "Qwedwe2";
        assertFalse(loginApiService.verifyLogin(FrameworkConfig.APP_USER_EMAIL, incorrectPassword));
    }

    @Test
    public void exceptionTest() {
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(null, FrameworkConfig.APP_USER_PASSWORD));
        assertThrows(RuntimeException.class, () -> loginApiService.verifyLogin(FrameworkConfig.APP_USER_EMAIL, ""));
    }
}

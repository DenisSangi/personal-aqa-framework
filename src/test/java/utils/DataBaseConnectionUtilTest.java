package utils;

import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class DataBaseConnectionUtilTest {

    DatabaseConnectionUtil dataBaseConnectionUtil = new DatabaseConnectionUtil(FrameworkConfig.DB_NAME, FrameworkConfig.DB_USERNAME, FrameworkConfig.DB_PASSWORD, FrameworkConfig.DB_HOST, FrameworkConfig.DB_PORT);
    private final String testingEmail = "test_user_1@gmail.com";

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {
        String restoreQuery = "UPDATE users SET birth_date = NULL WHERE email = ?";
        dataBaseConnectionUtil.executeUpdate(restoreQuery, testingEmail);
    }

    @Test
    public void testExecuteQuery() throws Exception {

        String query = "SELECT id, name, country FROM users ORDER BY id";
        String expectedCountry = "India";
        List<Map<String, String>> result = dataBaseConnectionUtil.executeQuery(query);
        assertEquals(result.size(), 3);
        assertEquals(result.get(0).get("country"), expectedCountry);
    }

    @Test
    public void testExecuteUpdate() throws Exception {

        String query = "UPDATE users SET birth_date = '2007-07-07' WHERE email = ?";
        int updatedRows = dataBaseConnectionUtil.executeUpdate(query, testingEmail);
        assertEquals(updatedRows, 1);
    }
}
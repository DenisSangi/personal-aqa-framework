package database;

import api.models.AccountFactory;
import api.models.AccountModel;
import config.FrameworkConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.DatabaseConnectionUtil;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class UserRecordLifecycleTest {

    DatabaseConnectionUtil dataBaseConnectionUtil = new DatabaseConnectionUtil(FrameworkConfig.DB_NAME, FrameworkConfig.DB_USERNAME, FrameworkConfig.DB_PASSWORD, FrameworkConfig.DB_HOST, FrameworkConfig.DB_PORT);
    private final AccountModel accountModelValid = AccountFactory.createValidAccountModel();

    private final String insertQuery = """
            INSERT INTO users (
            name,
            email,
            password,
            birth_date,
            first_name,
            last_name,
            address1,
            country,
            zipcode,
            state,
            city,
            mobile_phone)\s
            VALUES (
            ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
           \s""";

    private final String updatedPassword = "Asdf5567";
    private final Date birthDate = Date.valueOf(accountModelValid.getBirthYear() + "-" + accountModelValid.getBirthMonth() + "-" + accountModelValid.getBirthDate());
    private final String selectQuery = "SELECT * FROM users WHERE email='%s'".formatted(accountModelValid.getEmail());
    private final String updateQuery = "UPDATE users SET password = '%s' WHERE id = ?".formatted(updatedPassword);


    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws Exception {
        String deleteQuery = "DELETE FROM users WHERE email=?";
        if (!dataBaseConnectionUtil.executeQuery(selectQuery).isEmpty()) {
            dataBaseConnectionUtil.executeUpdate(deleteQuery, accountModelValid.getEmail());
        }
    }

    @Test
    public void userRecordLifecycleTest() throws Exception {
        //Step 1: INSERT a new testing row in users table
        int insertResult = dataBaseConnectionUtil.executeUpdate(insertQuery,
                accountModelValid.getName(),
                accountModelValid.getEmail(),
                accountModelValid.getPassword(),
                birthDate,
                accountModelValid.getFirstName(),
                accountModelValid.getLastName(),
                accountModelValid.getAddress1(),
                accountModelValid.getCountry(),
                accountModelValid.getZipCode(),
                accountModelValid.getState(),
                accountModelValid.getCity(),
                accountModelValid.getMobilePhone());
        assertEquals(insertResult, 1);

        //Step 2: SELECT added testing row verify it's content and hold it's id
        List<Map<String, String>> selectResult = dataBaseConnectionUtil.executeQuery(selectQuery);
        int rowId = Integer.parseInt(selectResult.get(0).get("id"));
        assertEquals(selectResult.get(0).get("title"), accountModelValid.getTitle());
        assertEquals(selectResult.get(0).get("name"), accountModelValid.getName());
        assertEquals(Date.valueOf(selectResult.get(0).get("birth_date")), birthDate);
        assertEquals(selectResult.get(0).get("zipcode"), accountModelValid.getZipCode());
        assertEquals(selectResult.get(0).get("company"), accountModelValid.getCompany());
        assertEquals(selectResult.get(0).get("address2"), accountModelValid.getAddress2());
        assertEquals(selectResult.get(0).get("country"), accountModelValid.getCountry());
        assertEquals(selectResult.get(0).get("state"), accountModelValid.getState());
        assertEquals(selectResult.get(0).get("city"), accountModelValid.getCity());
        assertEquals(selectResult.get(0).get("first_name"), accountModelValid.getFirstName());
        assertEquals(selectResult.get(0).get("last_name"), accountModelValid.getLastName());
        assertFalse(selectResult.get(0).get("id").isEmpty());
        assertFalse(selectResult.get(0).get("created_at").isEmpty());

        //Step 3: UPDATE testing row and verify update was successful
        int updateResult = dataBaseConnectionUtil.executeUpdate(updateQuery, rowId);
        assertEquals(updateResult, 1);
        List<Map<String, String>> selectResultAfterUpdate = dataBaseConnectionUtil.executeQuery(selectQuery);
        assertEquals(selectResultAfterUpdate.get(0).get("password"), updatedPassword);

        //Step 4: DELETE testing row and verify delete was successful
        String deleteQuery = "DELETE FROM users WHERE id=?";
        int deleteResult = dataBaseConnectionUtil.executeUpdate(deleteQuery, rowId);
        assertEquals(deleteResult, 1);
        List<Map<String, String>> selectResultAfterDelete = dataBaseConnectionUtil.executeQuery(selectQuery);
        assertTrue(selectResultAfterDelete.isEmpty());
    }
}

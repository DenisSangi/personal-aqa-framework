package api.services;

import api.core.BaseApiService;
import api.models.AccountModel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AccountApiService extends BaseApiService {

    private static final String CREATE_ACCOUNT_ENDPOINT = "/api/createAccount",
            DELETE_ACCOUNT_ENDPOINT = "/api/deleteAccount",
            GET_ACCOUNT_BY_EMAIL_ENDPOINT = "/api/getUserDetailByEmail",
            UPDATE_ACCOUNT_ENDPOINT = "/api/updateAccount";

    public void createAccount(AccountModel accountModel) {
        Response response = restClient.post(CREATE_ACCOUNT_ENDPOINT, buildFormParams(accountModel));

        if (response.jsonPath().getInt("responseCode") != 201) {
            throw new RuntimeException("Account wasn't created due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }

        log.debug("Create Account: {}", response.getBody().asString());
    }

    public void updateAccount(AccountModel accountModel) {
        Response response = restClient.put(UPDATE_ACCOUNT_ENDPOINT, buildFormParams(accountModel));

        if (response.jsonPath().getInt("responseCode") != 200) {
            throw new RuntimeException("Account wasn't updated due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }

        log.debug("Update Account: {}", response.getBody().asString());
    }

    public void deleteAccount(String email, String password) {
        Map<String, String> formParams = new HashMap<>();

        formParams.put("email", email);
        formParams.put("password", password);

        Response response = restClient.delete(DELETE_ACCOUNT_ENDPOINT, formParams);

        if (response.jsonPath().getInt("responseCode") != 200) {
            throw new RuntimeException("Account wasn't deleted due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }

        log.debug("Delete account: {}", response.getBody().asString());
    }

    public String getUserFirstnameByEmail(String email) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("email", email);

        Response response = restClient.get(GET_ACCOUNT_BY_EMAIL_ENDPOINT, queryParams);

        if (response.jsonPath().getInt("responseCode") != 200) {
            throw new RuntimeException("Unable to get account due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }

        log.debug("Get account's details: {}", response.getBody().asString());

        return response.jsonPath().getString("user.first_name");
    }

    private Map<String, String> buildFormParams(AccountModel model) {
        Map<String, String> formParams = new HashMap<>();

        formParams.put("email", model.getEmail());
        formParams.put("name", model.getName());
        formParams.put("password", model.getPassword());
        formParams.put("title", model.getTitle());
        formParams.put("birth_date", model.getBirthDate());
        formParams.put("birth_month", model.getBirthMonth());
        formParams.put("birth_year", model.getBirthYear());
        formParams.put("firstname", model.getFirstName());
        formParams.put("lastname", model.getLastName());
        formParams.put("company", model.getCompany());
        formParams.put("address1", model.getAddress1());
        formParams.put("address2", model.getAddress2());
        formParams.put("country", model.getCountry());
        formParams.put("state", model.getState());
        formParams.put("city", model.getCity());
        formParams.put("mobile_number", model.getMobilePhone());
        formParams.put("zipcode", model.getZipCode());

        return formParams;
    }
}

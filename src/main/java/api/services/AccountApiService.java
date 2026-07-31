package api.services;

import api.core.BaseApiService;
import api.models.AccountModel;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class AccountApiService extends BaseApiService {

    private static final String CREATE_ACCOUNT_ENDPOINT = "/api/createAccount",
    DELETE_ACCOUNT_ENDPOINT = "/api/deleteAccount";

    public void createAccount(AccountModel accountModel) {

        Map<String, String> formParams = new HashMap<>();

        formParams.put("email", accountModel.getEmail());
        formParams.put("name", accountModel.getName());
        formParams.put("password", accountModel.getPassword());
        formParams.put("title", accountModel.getTitle());
        formParams.put("birth_date", accountModel.getBirthDate());
        formParams.put("birth_month", accountModel.getBirthMonth());
        formParams.put("birth_year", accountModel.getBirthYear());
        formParams.put("firstname", accountModel.getFirstName());
        formParams.put("lastname", accountModel.getLastName());
        formParams.put("company", accountModel.getCompany());
        formParams.put("address1", accountModel.getAddress1());
        formParams.put("address2", accountModel.getAddress2());
        formParams.put("country", accountModel.getCountry());
        formParams.put("state", accountModel.getState());
        formParams.put("city", accountModel.getCity());
        formParams.put("mobile_number", accountModel.getMobilePhone());
        formParams.put("zipcode", accountModel.getZipCode());

        Response response = restClient.post(CREATE_ACCOUNT_ENDPOINT, formParams);

        System.out.println("Create Account: " + response.getBody().asString());

        if (response.jsonPath().getInt("responseCode") != 201) {
            throw new RuntimeException("Account wasn't created due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }
    }

    public void deleteAccount(String email, String password) {
        Map<String, String> formParams = new HashMap<>();

        formParams.put("email", email);
        formParams.put("password", password);

        Response response = restClient.delete(DELETE_ACCOUNT_ENDPOINT, formParams);

        System.out.println("Delete account: " + response.getBody().asString());

        if (response.jsonPath().getInt("responseCode")!=200) {
            throw new RuntimeException("Account wasn't deleted due to " + response.jsonPath().getInt("responseCode") + ": " + response.getBody().asString());
        }
    }
}

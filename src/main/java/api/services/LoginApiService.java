package api.services;

import api.core.BaseApiService;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class LoginApiService extends BaseApiService {

    private static final String LOGIN_ENDPOINT = "/api/verifyLogin";

    public boolean verifyLogin(String email, String password) {
        Map<String, String> formParams = new HashMap<>();

        if (email != null && !(email.isEmpty())) {
            formParams.put("email", email);
        }

        if (password != null && !(password.isEmpty())) {
            formParams.put("password", password);
        }

        Response response = restClient.post(LOGIN_ENDPOINT, formParams);

        if (response.jsonPath().getInt("responseCode") == 200) {
            return true;
        } else if (response.jsonPath().getInt("responseCode") == 404) {
            return false;
        } else {
            throw new RuntimeException("HTTP Error " + response.getStatusCode() + ": " + response.getBody().asString());
        }
    }
}

package api.services;

import api.core.BaseApiService;
import api.models.AccountCommonResponseModel;
import api.models.AccountModel;
import api.models.AccountResponseModel;
import api.models.ResponseAccountContainerModel;
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
        AccountCommonResponseModel accountCommonResponseModel = response.getBody().as(AccountCommonResponseModel.class);

        if (accountCommonResponseModel.getResponseCode() != 201) {
            throw new RuntimeException("Account wasn't created due to " + accountCommonResponseModel.getResponseCode() + ": " + accountCommonResponseModel.getMessage());
        }

        log.debug("Create Account: {}", accountCommonResponseModel.getMessage());
    }

    public void updateAccount(AccountModel accountModel) {
        Response response = restClient.put(UPDATE_ACCOUNT_ENDPOINT, buildFormParams(accountModel));
        AccountCommonResponseModel accountCommonResponseModel = response.getBody().as(AccountCommonResponseModel.class);

        if (accountCommonResponseModel.getResponseCode() != 200) {
            throw new RuntimeException("Account wasn't updated due to " + accountCommonResponseModel.getResponseCode() + ": " + accountCommonResponseModel.getMessage());
        }

        log.debug("Update Account: {}", accountCommonResponseModel.getMessage());
    }

    public void deleteAccount(String email, String password) {
        Map<String, String> formParams = new HashMap<>();

        formParams.put("email", email);
        formParams.put("password", password);

        Response response = restClient.delete(DELETE_ACCOUNT_ENDPOINT, formParams);
        AccountCommonResponseModel accountCommonResponseModel = response.getBody().as(AccountCommonResponseModel.class);

        if (accountCommonResponseModel.getResponseCode() != 200) {
            throw new RuntimeException("Account wasn't deleted due to " + accountCommonResponseModel.getResponseCode() + ": " + accountCommonResponseModel.getMessage());
        }

        log.debug("Delete account: {}", accountCommonResponseModel.getMessage());
    }

    /**
     * Построен поверх {@link #getAccountDetailsByEmail(String)}, а не поверх собственного запроса.
     * Раньше метод читал тело через {@code jsonPath().getString("user.first_name")} — путь-строка,
     * которую компилятор не проверяет; именно на ней однажды был получен тихий {@code null}
     * из-за пропущенного префикса {@code user.}. Теперь поле берётся из модели.
     */
    public String getUserFirstnameByEmail(String email) {

        AccountResponseModel accountResponseModel = getAccountDetailsByEmail(email);

        log.debug("Get account's first name: {}", accountResponseModel.getFirstName());

        return accountResponseModel.getFirstName();
    }

    /**
     * Принимает КОНВЕРТ, отдаёт ВЛОЖЕННУЮ модель — в этом вся асимметрия метода.
     * <p>
     * {@code as(...)} применяется к корню тела, поэтому разбирать обязан
     * {@link api.models.ResponseAccountContainerModel}. Но наружу конверт не отдаётся: он
     * транспортный, а {@code responseCode} уже проверен здесь же. Имя метода обещает детали
     * аккаунта — их и возвращаем.
     * <p>
     * Порядок строк важен: разбор тела идёт ДО проверки кода ответа, поэтому конверт обязан
     * описывать и ключи ветки ошибки ({@code message}) — иначе на любом не-200 разбор
     * развалится внутри Jackson, и проверка ниже не выполнится никогда.
     */
    public AccountResponseModel getAccountDetailsByEmail(String email) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("email", email);

        Response response = restClient.get(GET_ACCOUNT_BY_EMAIL_ENDPOINT, queryParams);
        ResponseAccountContainerModel responseAccountContainerModel = response.getBody().as(ResponseAccountContainerModel.class);

        if (responseAccountContainerModel.getResponseCode() != 200) {
            throw new RuntimeException("Unable to get account due to " + responseAccountContainerModel.getResponseCode() + ": " + responseAccountContainerModel.getMessage());
        }

        log.debug("Get account's details: {}", response.getBody().asString());

        return responseAccountContainerModel.getAccountResponseModel();
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

package api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class AccountModel {
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @Builder.Default
    private String title = "Mr.";
    @Builder.Default
    private String birthDate = "";
    @Builder.Default
    private String birthMonth = "";
    @Builder.Default
    private String birthYear = "";
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @Builder.Default
    private String company = "";
    @NonNull
    private String address1;
    @Builder.Default
    private String address2 = "";
    @NonNull
    private String country;
    @NonNull
    private String zipCode;
    @NonNull
    private String state;
    @NonNull
    private String city;
    @NonNull
    private String mobilePhone;
}

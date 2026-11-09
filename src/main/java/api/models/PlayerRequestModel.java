package api.models;

import lombok.Data;

@Data
public class PlayerRequestModel {

    private String username;
    private String email;
    private String country;
}

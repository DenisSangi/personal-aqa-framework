package api.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlayerResponseModel {

    private int id;
    private String username;
    private String email;
    private String country;
    private int level;
    private BigDecimal balance;
    private String createdAt;
}

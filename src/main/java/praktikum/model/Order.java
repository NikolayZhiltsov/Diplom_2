package praktikum.model;

import lombok.Data;

@Data
public class Order {
    private String accessToken;
    private String[] ingredients;
}

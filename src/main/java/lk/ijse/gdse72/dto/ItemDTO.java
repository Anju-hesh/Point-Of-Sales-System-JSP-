package lk.ijse.gdse72.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter

public class ItemDTO {

    private String code;
    private String name;
    private double price;
    private int qtyOnHand;
}

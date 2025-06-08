package lk.ijse.gdse72.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter

public class ItemTM {

    private String code;
    private String name;
    private double price;
    private int qtyOnHand;
}

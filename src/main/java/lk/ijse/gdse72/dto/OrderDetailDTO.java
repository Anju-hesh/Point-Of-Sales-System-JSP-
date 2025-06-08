package lk.ijse.gdse72.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter

public class OrderDetailDTO {
    private String orderId;
    private ItemDTO item;
    private int qty;
}

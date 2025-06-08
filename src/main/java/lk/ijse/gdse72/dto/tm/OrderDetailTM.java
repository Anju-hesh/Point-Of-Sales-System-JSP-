package lk.ijse.gdse72.dto.tm;

import lk.ijse.gdse72.dto.ItemDTO;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class OrderDetailTM {
    private String orderId;
    private ItemDTO item;
    private int qty;
}

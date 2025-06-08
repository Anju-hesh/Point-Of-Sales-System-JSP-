package lk.ijse.gdse72.dto;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {

    private String orderId;
    private CustomerDTO customer;
    private List<OrderDetailDTO> orderDetails;
    private Date date;
}

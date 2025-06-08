package lk.ijse.gdse72.dto.tm;

import lk.ijse.gdse72.dto.CustomerDTO;
import lk.ijse.gdse72.dto.OrderDetailDTO;
import lombok.*;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter

public class OrderTM {

    private String orderId;
    private CustomerDTO customer;
    private List<OrderDetailDTO> orderDetails;
    private Date date;

}

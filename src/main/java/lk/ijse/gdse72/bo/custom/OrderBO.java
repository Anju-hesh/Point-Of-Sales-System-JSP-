package lk.ijse.gdse72.bo.custom;

import lk.ijse.gdse72.bo.SuperBO;
import lk.ijse.gdse72.dto.OrderDTO;

import java.util.List;

public interface OrderBO extends SuperBO {
    String generateNewOrderId();
    boolean placeOrder(OrderDTO orderDTO) throws Exception;
}

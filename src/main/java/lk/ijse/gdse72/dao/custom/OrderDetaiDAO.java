package lk.ijse.gdse72.dao.custom;

import lk.ijse.gdse72.dao.CrudDAO;
import lk.ijse.gdse72.entity.OrderDetail;

public interface OrderDetaiDAO extends CrudDAO<OrderDetail , String> {

    OrderDetail searchOD(String orderId, String itemCode) throws Exception;
}

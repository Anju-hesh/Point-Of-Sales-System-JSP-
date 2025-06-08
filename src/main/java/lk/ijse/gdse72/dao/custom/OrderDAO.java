package lk.ijse.gdse72.dao.custom;

import lk.ijse.gdse72.dao.CrudDAO;
import lk.ijse.gdse72.entity.Orders;

public interface OrderDAO extends CrudDAO<Orders, String> {

    String getLastOrderCode();

//    boolean saveOrder(Orders order);
}

package lk.ijse.gdse72.bo;

import lk.ijse.gdse72.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse72.bo.custom.impl.ItemBOImpl;
import lk.ijse.gdse72.bo.custom.impl.OrderBOImpl;
import lk.ijse.gdse72.bo.custom.impl.OrderDetailBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOType {
        CUSTOMER,
        ITEM,
        ORDER,
        ORDER_DETAIL,
    }

    public SuperBO getBO(BOType type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case ORDER:
                return new OrderBOImpl();
            case ORDER_DETAIL:
                return new OrderDetailBOImpl();
            default:
                return null;
        }
    }
}

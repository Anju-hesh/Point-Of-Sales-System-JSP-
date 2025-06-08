package lk.ijse.gdse72.dao;

import lk.ijse.gdse72.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.gdse72.dao.custom.impl.ItemDAOImpl;
import lk.ijse.gdse72.dao.custom.impl.OrderDAOImpl;
import lk.ijse.gdse72.dao.custom.impl.OrderDetailDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public enum DaoType {
        CUSTOMER,
        ITEM,
        ORDER,
        ORDER_DETAIL,
    }

    public <T extends SuperDAO> T getDAO(DaoType type) {
        return switch (type) {
            case CUSTOMER -> (T) new CustomerDAOImpl();
            case ITEM -> (T) new ItemDAOImpl();
            case ORDER -> (T) new OrderDAOImpl();
            case ORDER_DETAIL -> (T) new OrderDetailDAOImpl();
        };
    }
}

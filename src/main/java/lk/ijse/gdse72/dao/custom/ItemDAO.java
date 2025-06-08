package lk.ijse.gdse72.dao.custom;

import lk.ijse.gdse72.dao.CrudDAO;
import lk.ijse.gdse72.entity.Item;

public interface ItemDAO extends CrudDAO<Item, String> {
    String getLastItemCode();
}

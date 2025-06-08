package lk.ijse.gdse72.bo.custom;

import lk.ijse.gdse72.bo.SuperBO;
import lk.ijse.gdse72.dto.ItemDTO;

import java.util.List;

public interface ItemBO extends SuperBO {
    String generateNewItemId();
    boolean saveItem(ItemDTO dto) throws Exception;
    List<ItemDTO> getAllItems() throws Exception;
    ItemDTO getItem(String code) throws Exception;
    boolean updateItem(ItemDTO itemDTO) throws Exception;
    boolean deleteItem(String itemCode);
}

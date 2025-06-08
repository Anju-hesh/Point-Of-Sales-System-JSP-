package lk.ijse.gdse72.bo.custom.impl;

import lk.ijse.gdse72.bo.custom.ItemBO;
import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.DAOFactory;
import lk.ijse.gdse72.dao.custom.ItemDAO;
import lk.ijse.gdse72.dto.ItemDTO;
import lk.ijse.gdse72.entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ItemBOImpl implements ItemBO {

    private final ItemDAO ITEMDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.ITEM);

    @Override
    public String generateNewItemId() {
        System.out.println("Before ITEMDAO.getLastItemCode() call");
        String lastCode = ITEMDAO.getLastItemCode();
        System.out.println("Last Item Code: " + lastCode);

        if (lastCode != null) {
            try {
                int newItemId = Integer.parseInt(lastCode.substring(4)) + 1;
                System.out.println("New Item ID: " + newItemId);
                return String.format("ITM-%03d", newItemId);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid item code format: " + lastCode, e);
            }
        } else {
            return "ITM-001";
        }
    }

    @Override
    public boolean saveItem(ItemDTO dto) throws Exception {

        System.out.println("Before Call ITEMDAO.save()");

        return ITEMDAO.save(
                new Item(
                        dto.getCode(),
                        dto.getName(),
                        dto.getPrice(),
                        dto.getQtyOnHand()
                )
        );
    }

    @Override
    public List<ItemDTO> getAllItems() throws Exception {
        System.out.println("Before Call ITEMDAO.getAll()");

        List<Item> allItems = ITEMDAO.getAll();
        System.out.println("Items retrieved: " + allItems.size());

        return allItems.stream()
                .map(item -> new ItemDTO(
                        item.getCode(),
                        item.getName(),
                        item.getPrice(),
                        item.getQtyOnHand()
                ))
                .toList();
    }

    @Override
    public ItemDTO getItem(String code) throws Exception {
        System.out.println("Before Call ITEMDAO.search()");

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            ITEMDAO.setSession(session);

            Item item = ITEMDAO.search(code);
            if (item != null) {
                ItemDTO itemDTO = new ItemDTO(
                        item.getCode(),
                        item.getName(),
                        item.getPrice(),
                        item.getQtyOnHand()
                );
                transaction.commit();
                return itemDTO;
            } else {
                transaction.rollback();
                throw new Exception("Item not found with code: " + code);
            }

        } catch (Exception e) {
            transaction.rollback();
            throw new Exception("Failed to retrieve item: " + e.getMessage(), e);

        } finally {
            session.close();
            ITEMDAO.setSession(null);
        }
    }


    @Override
    public boolean updateItem(ItemDTO itemDTO) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            ITEMDAO.setSession(session);

            System.out.println("Before Call ITEMDAO.update()");

            boolean result = ITEMDAO.update(
                    new Item(
                            itemDTO.getCode(),
                            itemDTO.getName(),
                            itemDTO.getPrice(),
                            itemDTO.getQtyOnHand()
                    )
            );

            transaction.commit();
            return result;
        } catch (Exception e) {
            transaction.rollback();
            throw new Exception("Failed to update item: " + e.getMessage(), e);
        } finally {
            session.close();
            ITEMDAO.setSession(null);
        }
    }


    @Override
    public boolean deleteItem(String itemCode) {
        System.out.println("Before Call ITEMDAO.delete()");

        try {
            return ITEMDAO.delete(itemCode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Item with code: " + itemCode, e);
        }
    }
}

package lk.ijse.gdse72.dao.custom;

import lk.ijse.gdse72.dao.CrudDAO;
import lk.ijse.gdse72.entity.Customer;

import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer, String> {
//    boolean save(Customer customer) throws Exception;
//    boolean update(Customer customer) throws Exception;
//    boolean delete(String id) throws Exception;
//    Customer search(String id) throws Exception;
//    List<Customer> getAll() throws Exception;
    String getLastCustomerId() throws Exception;

}

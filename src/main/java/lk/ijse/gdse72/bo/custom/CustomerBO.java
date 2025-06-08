package lk.ijse.gdse72.bo.custom;

import lk.ijse.gdse72.bo.SuperBO;
import lk.ijse.gdse72.dto.CustomerDTO;

import java.util.List;

public interface CustomerBO extends SuperBO {

    boolean saveCustomer(CustomerDTO dto);
    boolean updateCustomer(CustomerDTO dto);
    boolean deleteCustomer(String id);
    CustomerDTO getCustomer(String id);
    List<CustomerDTO> getAllCustomers();
    String generateNewCustomerId() throws Exception;
//    List<String> getAllCustomerIds();
}

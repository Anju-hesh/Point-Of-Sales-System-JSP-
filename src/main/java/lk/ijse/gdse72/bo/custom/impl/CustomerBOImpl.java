package lk.ijse.gdse72.bo.custom.impl;

import lk.ijse.gdse72.bo.custom.CustomerBO;
import lk.ijse.gdse72.dao.DAOFactory;
import lk.ijse.gdse72.dao.custom.CustomerDAO;
import lk.ijse.gdse72.dto.CustomerDTO;
import lk.ijse.gdse72.entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CustomerBOImpl implements CustomerBO {

    private static final Logger logger = Logger.getLogger(CustomerBOImpl.class.getName());
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.CUSTOMER);

    public CustomerBOImpl() {}

    @Override
    public boolean saveCustomer(CustomerDTO dto) {
        if (dto == null) {
            logger.warning("Attempted to save null CustomerDTO");
            return false;
        }

        if (!isValidCustomerDTO(dto)) {
            logger.warning("Invalid CustomerDTO provided for save operation");
            return false;
        }

        try {
            Customer customer = convertToEntity(dto);
            boolean result = customerDAO.save(customer);

            if (result) {
                logger.info("Customer saved successfully: " + dto.getId());
            } else {
                logger.warning("Failed to save customer: " + dto.getId());
            }

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving customer: " + dto.getId(), e);
            return false;
        }
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) {
        if (dto == null) {
            logger.warning("Attempted to update null CustomerDTO");
            return false;
        }

        if (!isValidCustomerDTO(dto)) {
            logger.warning("Invalid CustomerDTO provided for update operation");
            return false;
        }

        try {
            System.out.println("CostomerBOImpl Try Block Before go to the DAO");
            Customer customer = convertToEntity(dto);
            boolean result = customerDAO.update(customer);
            System.out.println("CostomerBOImpl Try Block After go to the DAO");

            if (result) {
                logger.info("Customer updated successfully: " + dto.getId());
            } else {
                logger.warning("Failed to update customer: " + dto.getId());
            }

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating customer: " + dto.getId(), e);
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(String id) {
        if (id == null || id.isEmpty()) {
            logger.warning("Attempted to delete customer with null or empty ID");
            return false;
        }

        try {
//            System.out.println("CostomerBOImpl Try Block Before go to the DAO");
            boolean result = customerDAO.delete(id);

            if (result) {
                logger.info("Customer deleted successfully: " + id);
            } else {
                logger.warning("Failed to delete customer: " + id);
            }

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting customer: " + id, e);
            return false;
        }
    }

    @Override
    public CustomerDTO getCustomer(String id) {
        if (id == null || id.trim().isEmpty()) {
//            logger.warning("Attempted to get customer with null or empty ID");
            System.out.println("Attempted to get customer with null or empty ID");
            return null;
        }

        try {
            Customer entity = customerDAO.search(id);

            if (entity != null) {
                CustomerDTO dto = convertToDTO(entity);
//                logger.info("Customer retrieved successfully: " + id);
                System.out.println("Customer retrieved successfully: " + id);
                return dto;
            } else {
                logger.info("Customer not found: " + id);
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving customer: " + id, e);
            return null;
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {

        try {

//            System.out.println("CostomerBOImpl Try Block Before go to the DAO");

            List<CustomerDTO> dtoList = new ArrayList<>();
            List<Customer> entities = customerDAO.getAll();

//            System.out.println("Retrieved " + entities.size() + " customers from the database");

            if (entities != null) {
//                System.out.println("Converting Customer entities to DTOs");
                for (Customer entity : entities) {
                    dtoList.add(convertToDTO(entity));
                }
            }

//            logger.info("Retrieved " + dtoList.size() + " customers");
            return dtoList;
        } catch (Exception e) {
//            System.out.println("Error retrieving all customers: " + e.getMessage());
            e.printStackTrace();
//            logger.log(Level.SEVERE, "Error retrieving all customers", e);
            return new ArrayList<>();
        }
    }

    private boolean isValidCustomerDTO(CustomerDTO dto) {
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return false;
        }
        if (dto.getName() == null || dto.getName().isEmpty()) {
            return false;
        }
        if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
            return false;
        }
        if (dto.getSalary() < 0) {
            return false;
        }
        return true;
    }

    private Customer convertToEntity(CustomerDTO dto) {
        return new Customer(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary()
        );
    }

    private CustomerDTO convertToDTO(Customer entity) {
        return new CustomerDTO(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getSalary()
        );
    }

    @Override
    public String generateNewCustomerId() throws Exception {
        String lastId = customerDAO.getLastCustomerId();
//        System.out.println("Last Customer ID: " + lastId);

        if (lastId != null) {
            int num = Integer.parseInt(lastId.substring(1)) + 1;
//            System.out.println("New Customer ID Number: " + num);

            return String.format("C%03d", num);
        } else {
            return "C001";
        }
    }

//    @Override
//    public List<String> getAllCustomerIds() {
//        try {
//            List<Customer> customers = customerDAO.getAll();
//            List<String> ids = new ArrayList<>();
//
//            for (Customer customer : customers) {
//                ids.add(customer.getId());
//            }
//
//            return ids;
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Error retrieving all customer IDs", e);
//            return new ArrayList<>();
//        }
//    }
}
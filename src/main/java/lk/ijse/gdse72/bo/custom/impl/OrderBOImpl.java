package lk.ijse.gdse72.bo.custom.impl;

import lk.ijse.gdse72.bo.custom.OrderBO;
import lk.ijse.gdse72.config.FactoryConfiguration;
import lk.ijse.gdse72.dao.DAOFactory;
import lk.ijse.gdse72.dao.custom.CustomerDAO;
import lk.ijse.gdse72.dao.custom.ItemDAO;
import lk.ijse.gdse72.dao.custom.OrderDAO;
import lk.ijse.gdse72.dao.custom.OrderDetaiDAO;
import lk.ijse.gdse72.dto.OrderDTO;
import lk.ijse.gdse72.dto.OrderDetailDTO;
import lk.ijse.gdse72.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderBOImpl implements OrderBO {

    private final OrderDAO ORDERDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.ORDER);
    private final OrderDetaiDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.ORDER_DETAIL);
    private final ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.ITEM);
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOFactory.DaoType.CUSTOMER);

    @Override
    public String generateNewOrderId() {
        System.out.println("Before ORDERDAO.getLastOrderCode() call");

        String lastCode = ORDERDAO.getLastOrderCode();

        System.out.println("Last Order Code: " + lastCode);

        if (lastCode != null) {
            try {
                int newOrderId = Integer.parseInt(lastCode.substring(4)) + 1;
                System.out.println("New Order ID: " + newOrderId);
                return String.format("ORD-%03d", newOrderId);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid order code format: " + lastCode, e);
            }
        } else {
            return "ORD-001";
        }
    }

//    @Override
//    public boolean placeOrder(OrderDTO orderDTO) throws Exception {
//
//        Session session = FactoryConfiguration.getInstance().getSession();
//        Transaction transaction = session.beginTransaction();
//
//        try {
//            ORDERDAO.setSession(session);
//            orderDetailDAO.setSession(session);
//            itemDAO.setSession(session);
//            customerDAO.setSession(session);
//
//            Customer customer = customerDAO.search(orderDTO.getCustomer().getId());
//            System.out.println("customer: " + customer);
//
//            if (customer == null) {
//                throw new Exception("Customer not found: " + orderDTO.getCustomer().getId());
//            }
//
//            for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
//                Item item = itemDAO.search(detail.getItem().getCode());
//                if (item == null) {
//                    throw new Exception("Item not found: " + detail.getItem().getCode());
//                }
//                if (item.getQtyOnHand() < detail.getQty()) {
//                    throw new Exception("Insufficient stock for item: " + detail.getItem().getCode() +
//                            ". Available: " + item.getQtyOnHand() + ", Required: " + detail.getQty());
//                }
//            }
//
//            Orders order = new Orders(
//                    orderDTO.getOrderId(),
//                    orderDTO.getDate(),
//                    customer
//            );
//
//            ORDERDAO.save(order);
//
//            for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
//                Item item = itemDAO.search(detailDTO.getItem().getCode());
//
//                OrderDetail orderDetail = new OrderDetail(
//                        orderDTO.getOrderId(),
//                        item,
//                        detailDTO.getQty(),
//                        order
//                );
//
//                orderDetailDAO.save(orderDetail);
//
//                item.setQtyOnHand(item.getQtyOnHand() - detailDTO.getQty());
//                itemDAO.update(item);
//            }
//
//            transaction.commit();
//            return true;
//
//        } catch (Exception e) {
//
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            throw new Exception("Failed to place order: " + e.getMessage(), e);
//        } finally {
//
//            ORDERDAO.setSession(null);
//            orderDetailDAO.setSession(null);
//            itemDAO.setSession(null);
//            customerDAO.setSession(null);
//
//            if (session != null) {
//                session.close();
//            }
//        }
//    }

    @Override
    public boolean placeOrder(OrderDTO orderDTO) throws Exception {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {

            ORDERDAO.setSession(session);
            orderDetailDAO.setSession(session);
            itemDAO.setSession(session);
            customerDAO.setSession(session);

            System.out.println("Starting order placement for Order ID: " + orderDTO.getOrderId());

            Customer customer = customerDAO.search(orderDTO.getCustomer().getId());
            if (customer == null) {
                throw new Exception("Customer not found: " + orderDTO.getCustomer().getId());
            }
            System.out.println("Customer found: " + customer.getId());

            for (OrderDetailDTO detail : orderDTO.getOrderDetails()) {
                Item item = itemDAO.search(detail.getItem().getCode());
                if (item == null) {
                    throw new Exception("Item not found: " + detail.getItem().getCode());
                }
                if (item.getQtyOnHand() < detail.getQty()) {
                    throw new Exception("Insufficient stock for item: " + detail.getItem().getCode() +
                            ". Available: " + item.getQtyOnHand() + ", Required: " + detail.getQty());
                }
                System.out.println("Item validated: " + item.getCode() + ", Stock: " + item.getQtyOnHand());
            }

            Orders order = new Orders(
                    orderDTO.getOrderId(),
                    orderDTO.getDate(),
                    customer
            );

            boolean orderSaved = ORDERDAO.save(order);
            if (!orderSaved) {
                throw new Exception("Failed to save main order");
            }
            System.out.println("Main order saved successfully: " + orderDTO.getOrderId());

            for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
                System.out.println("Processing item: " + detailDTO.getItem().getCode());

                Item item = itemDAO.search(detailDTO.getItem().getCode());
                if (item == null) {
                    throw new Exception("Item disappeared: " + detailDTO.getItem().getCode());
                }

                OrderDetail orderDetail = new OrderDetail(
                        orderDTO.getOrderId(),
                        item,
                        detailDTO.getQty(),
                        order
                );

                if (orderDetail.getId() == null) {
                    System.err.println("WARNING: OrderDetail ID is null!");
                    orderDetail.setId(new OrderDetailPK(orderDTO.getOrderId(), item.getCode()));
                }

                System.out.println("OrderDetail ID: " + orderDetail.getId());
                System.out.println("Order ID in detail: " + orderDetail.getId().getOrderId());
                System.out.println("Item Code in detail: " + orderDetail.getId().getItemCode());

                boolean detailSaved = orderDetailDAO.save(orderDetail);
                if (!detailSaved) {
                    throw new Exception("Failed to save order detail for item: " + item.getCode());
                }
                System.out.println("Order detail saved for item: " + item.getCode());

                int oldQty = item.getQtyOnHand();
                item.setQtyOnHand(item.getQtyOnHand() - detailDTO.getQty());

                boolean itemUpdated = itemDAO.update(item);
                if (!itemUpdated) {
                    throw new Exception("Failed to update stock for item: " + item.getCode());
                }
                System.out.println("Item stock updated: " + item.getCode() +
                        " (Old: " + oldQty + ", New: " + item.getQtyOnHand() + ")");
            }

            session.flush();

            transaction.commit();
            System.out.println("Order placed successfully: " + orderDTO.getOrderId());
            return true;

        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            e.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
                System.out.println("Transaction rolled back");
            }
            throw new Exception("Failed to place order: " + e.getMessage(), e);

        } finally {

            ORDERDAO.setSession(null);
            orderDetailDAO.setSession(null);
            itemDAO.setSession(null);
            customerDAO.setSession(null);

            if (session != null) {
                session.close();
            }
        }
    }
}

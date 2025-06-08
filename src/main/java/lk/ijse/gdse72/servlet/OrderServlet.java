//package lk.ijse.gdse72.servlet;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lk.ijse.gdse72.bo.BOFactory;
//import lk.ijse.gdse72.bo.custom.OrderBO;
//import lk.ijse.gdse72.dto.OrderDTO;
//
//import java.io.IOException;
//
//@WebServlet(urlPatterns = "/order")
//public class OrderServlet extends HttpServlet {
//
//    private final OrderBO ORDERBO = (OrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDER);
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//            ObjectMapper mapper = new ObjectMapper();
//            OrderDTO orderDTO = mapper.readValue(req.getReader(), OrderDTO.class);
//
//        boolean isSaved = false;
//        try {
//            isSaved = ORDERBO.placeOrder(orderDTO);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        resp.setContentType("application/json");
//
//            if (isSaved) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write("{\"message\":\"Success\"}");
//            } else {
//                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                resp.getWriter().write("{\"message\":\"Failed\"}");
//            }
//        }
//    }

package lk.ijse.gdse72.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.OrderBO;
import lk.ijse.gdse72.bo.custom.CustomerBO;
import lk.ijse.gdse72.bo.custom.ItemBO;
import lk.ijse.gdse72.dto.OrderDTO;
import lk.ijse.gdse72.dto.OrderDetailDTO;
import lk.ijse.gdse72.dto.CustomerDTO;
import lk.ijse.gdse72.dto.ItemDTO;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/purchase-order", "/order"}) // Added both endpoints
public class OrderServlet extends HttpServlet {

    private final OrderBO orderBO = (OrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDER);
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);
    private final ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            // Parse the incoming JSON request
//            Map<String, Object> requestData = mapper.readValue(req.getReader(), Map.class);
//
//            String orderId = (String) requestData.get("orderId");
//            String customerId = (String) requestData.get("customerId");
//            String orderDate = (String) requestData.get("orderDate");
//            List<Map<String, Object>> items = (List<Map<String, Object>>) requestData.get("items");
//
//            // Validate input
//            if (orderId == null || customerId == null || orderDate == null || items == null || items.isEmpty()) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                mapper.writeValue(resp.getWriter(), Map.of(
//                        "success", false,
//                        "message", "Missing required fields"
//                ));
//                return;
//            }
//
//            // Get customer details
//            CustomerDTO customer = customerBO.getCustomer(customerId);
//            if (customer == null) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                mapper.writeValue(resp.getWriter(), Map.of(
//                        "success", false,
//                        "message", "Customer not found"
//                ));
//                return;
//            }
//
//            // Create order details
//            List<OrderDetailDTO> orderDetails = new ArrayList<>();
//            for (Map<String, Object> itemMap : items) {
//                String itemCode = (String) itemMap.get("code");
//                int qty = Integer.parseInt(itemMap.get("qty").toString());
//
//                // Get item details
//                ItemDTO item = itemBO.getItem(itemCode);
//                if (item == null) {
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    mapper.writeValue(resp.getWriter(), Map.of(
//                            "success", false,
//                            "message", "Item not found: " + itemCode
//                    ));
//                    return;
//                }
//
//                // Check stock availability
//                if (item.getQtyOnHand() < qty) {
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    mapper.writeValue(resp.getWriter(), Map.of(
//                            "success", false,
//                            "message", "Insufficient stock for item: " + itemCode
//                    ));
//                    return;
//                }
//
//                OrderDetailDTO orderDetail = new OrderDetailDTO(orderId, item, qty);
//                orderDetails.add(orderDetail);
//            }
//
//            // Create order DTO
//            OrderDTO orderDTO = new OrderDTO(
//                    orderId,
//                    customer,
//                    orderDetails,
//                    Date.valueOf(LocalDate.parse(orderDate))
//            );
//
//            // Place the order
//            boolean success = orderBO.placeOrder(orderDTO);
//
//            if (success) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                mapper.writeValue(resp.getWriter(), Map.of(
//                        "success", true,
//                        "message", "Order placed successfully",
//                        "orderId", orderId
//                ));
//            } else {
//                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                mapper.writeValue(resp.getWriter(), Map.of(
//                        "success", false,
//                        "message", "Failed to place order"
//                ));
//            }
//
//        } catch (Exception e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            mapper.writeValue(resp.getWriter(), Map.of(
//                    "success", false,
//                    "message", "Internal server error: " + e.getMessage()
//            ));
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> requestData = mapper.readValue(req.getReader(), Map.class);

            String orderId = (String) requestData.get("orderId");
            String customerId = (String) requestData.get("customerId");
            String orderDate = (String) requestData.get("orderDate");

            Object itemsObj = requestData.get("items");
            List<Map<String, Object>> items;

            if (itemsObj instanceof List<?>) {
                items = new ArrayList<>();
                for (Object obj : (List<?>) itemsObj) {
                    if (obj instanceof Map<?, ?>) {
                        items.add((Map<String, Object>) obj);
                    }
                }
            } else {
                items = Collections.emptyList();
            }

            if (orderId == null || customerId == null || orderDate == null || items.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(), Map.of(
                        "success", false,
                        "message", "Missing required fields or empty items list"
                ));
                return;
            }

            CustomerDTO customer = customerBO.getCustomer(customerId);

            if (customer == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(), Map.of(
                        "success", false,
                        "message", "Customer not found"
                ));
                return;
            }

            List<OrderDetailDTO> orderDetails = new ArrayList<>();
            for (Map<String, Object> itemMap : items) {
                String itemCode = (String) itemMap.get("code");
                int qty;

                try {
                    qty = Integer.parseInt(itemMap.get("qty").toString());
                } catch (NumberFormatException nfe) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(resp.getWriter(), Map.of(
                            "success", false,
                            "message", "Invalid quantity for item: " + itemCode
                    ));
                    return;
                }

                ItemDTO item = itemBO.getItem(itemCode);

                if (item == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(resp.getWriter(), Map.of(
                            "success", false,
                            "message", "Item not found: " + itemCode
                    ));
                    return;
                }

                if (item.getQtyOnHand() < qty) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(resp.getWriter(), Map.of(
                            "success", false,
                            "message", "Insufficient stock for item: " + itemCode
                    ));
                    return;
                }

                OrderDetailDTO orderDetail = new OrderDetailDTO(orderId, item, qty);
                orderDetails.add(orderDetail);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate parsedDate;
            try {
                parsedDate = LocalDate.parse(orderDate, formatter);
            } catch (DateTimeParseException dtpe) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(), Map.of(
                        "success", false,
                        "message", "Invalid date format. Please use MM/dd/yyyy"
                ));
                return;
            }

            OrderDTO orderDTO = new OrderDTO(
                    orderId,
                    customer,
                    orderDetails,
                    Date.valueOf(parsedDate)
            );

            boolean success = orderBO.placeOrder(orderDTO);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(resp.getWriter(), Map.of(
                        "success", true,
                        "message", "Order placed successfully",
                        "orderId", orderId
                ));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                mapper.writeValue(resp.getWriter(), Map.of(
                        "success", false,
                        "message", "Failed to place order"
                ));
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "success", false,
                    "message", "Internal server error: " + e.getMessage()
            ));
            e.printStackTrace();
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Generate new order ID
            String newOrderId = orderBO.generateNewOrderId();
            mapper.writeValue(resp.getWriter(), Map.of(
                    "success", true,
                    "orderId", newOrderId
            ));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "success", false,
                    "message", "Failed to generate order ID"
            ));
        }
    }
}

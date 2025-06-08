package lk.ijse.gdse72.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.CustomerBO;
import lk.ijse.gdse72.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse72.dto.CustomerDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private CustomerBO customerBO ;

    @Override
    public void init() throws ServletException {

        customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try {
            String customerId = request.getParameter("id");

            if (customerId != null && !customerId.isEmpty()) {
                CustomerDTO customer = customerBO.getCustomer(customerId);

                if (customer != null) {
                    String jsonResponse = objectMapper.writeValueAsString(customer);
                    response.getWriter().write(jsonResponse);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Customer not found");
                    response.getWriter().write(objectMapper.writeValueAsString(error));
                }
            } else {

                List<CustomerDTO> customers = customerBO.getAllCustomers();
                String jsonResponse = objectMapper.writeValueAsString(customers);
                response.getWriter().write(jsonResponse);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve customers");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");

            if ("save".equals(action)) {

                CustomerDTO dto = new CustomerDTO(
                        request.getParameter("id"),
                        request.getParameter("name"),
                        request.getParameter("address"),
                        Double.parseDouble(request.getParameter("salary"))
                );

                boolean success = customerBO.saveCustomer(dto);

                Map<String, Object> jsonResponse = new HashMap<>();
                if (success) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Customer saved successfully");
                    response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to save customer");
                    response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            StringBuilder requestBody = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            Map<String, String> params = new HashMap<>();
            if (requestBody.length() > 0) {
                String[] pairs = requestBody.toString().split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(URLDecoder.decode(keyValue[0], "UTF-8"),
                                URLDecoder.decode(keyValue[1], "UTF-8"));
                    }
                }
            }

            String action = params.get("action");
            System.out.println("Action in doPut: " + action);

            if ("update".equals(action)) {
                CustomerDTO dto = new CustomerDTO(
                        params.get("id"),
                        params.get("name"),
                        params.get("address"),
                        Double.parseDouble(params.get("salary"))
                );

                System.out.println("CustomerDTO in doPut: " + dto);
                System.out.println("before calling updateCustomer in CustomerBOImpl");
                boolean success = customerBO.updateCustomer(dto);

                Map<String, Object> jsonResponse = new HashMap<>();
                if (success) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Customer updated successfully");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Failed to update customer");
                }

                response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error: " + e.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String id = request.getParameter("id");
//            System.out.println("Before deleting customer with ID previous CustomerBO call: " + id);

            boolean success = customerBO.deleteCustomer(id);
//            System.out.println("After deleting customer with ID after CustomerBo Called: " + id);

            Map<String, Object> jsonResponse = new HashMap<>();
            if (success) {
//                System.out.println("Customer deleted successfully with ID: " + id);

                jsonResponse.put("success", true);
                jsonResponse.put("message", "Customer deleted successfully");
                response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to delete customer");
                response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }
    }
}
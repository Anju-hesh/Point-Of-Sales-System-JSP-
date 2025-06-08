package lk.ijse.gdse72.servlet.idservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.CustomerBO;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/customer-id")
public class CustomerIdServlet extends HttpServlet {

    private final CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOType.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
//            List<String> existingIds = customerBO.getAllCustomerIds();
            String newId = customerBO.generateNewCustomerId();
            System.out.println("CustomerIdServlet: Generated new customer ID: " + newId);
            resp.getWriter().write("{\"customerId\":\"" + newId + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to generate ID\"}");
        }
    }
}


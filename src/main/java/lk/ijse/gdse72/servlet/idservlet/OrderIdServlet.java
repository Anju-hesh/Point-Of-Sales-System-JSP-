package lk.ijse.gdse72.servlet.idservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.OrderBO;

import java.io.IOException;

@WebServlet(urlPatterns = "/order-id")
public class OrderIdServlet extends HttpServlet {

    private final OrderBO ORDERBO = (OrderBO) BOFactory.getInstance().getBO(BOFactory.BOType.ORDER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String newId = ORDERBO.generateNewOrderId();
            System.out.println("Generated Order ID: " + newId);
            resp.getWriter().write("{\"orderId\":\"" + newId + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to generate ID\"}");
        }
    }
}

package lk.ijse.gdse72.servlet.idservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.ItemBO;

import java.io.IOException;

@WebServlet(urlPatterns = "/item-code")
public class ItemCodeServlet extends HttpServlet {

    private final ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            System.out.println("Before ItemBO.generateNewItemId() call");
            String newId = itemBO.generateNewItemId();
            System.out.println("ItemCodeServlet: Generated new item ID: " + newId);
            resp.getWriter().write("{\"itemCode\":\"" + newId + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Failed to generate ID\"}");
        }
    }
}

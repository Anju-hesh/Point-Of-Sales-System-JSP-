package lk.ijse.gdse72.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse72.bo.BOFactory;
import lk.ijse.gdse72.bo.custom.ItemBO;
import lk.ijse.gdse72.dto.ItemDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/item"})
public class ItemServlet extends HttpServlet {

    private final ItemBO ITEMBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> item = mapper.readValue(req.getInputStream(), Map.class);

        resp.setContentType("application/json");

        ItemDTO itemDTO = new ItemDTO(
                (String) item.get("code"),
                (String) item.get("name"),
                Double.parseDouble(item.get("price").toString()),
                Integer.parseInt(item.get("qty").toString())
        );

        boolean success = false;
        try {
            System.out.println("Before ITEMBO.saveItem() call");
            success = ITEMBO.saveItem(itemDTO);
            System.out.println("Item saved successfully: " + success);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(success){
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "200",
                    "status", "success",
                    "message", "Item saved successfully"
            ));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "400",
                    "status", "failed",
                    "message", "Item not saved"
            ));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String itemCode = request.getParameter("code");

            if (itemCode != null && !itemCode.isEmpty()) {

                ItemDTO item = ITEMBO.getItem(itemCode);
                System.out.println("Retrieved item: " + item);

                if (item != null) {
                    objectMapper.writeValue(response.getWriter(), item);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    objectMapper.writeValue(response.getWriter(), Map.of(
                            "code", "404",
                            "status", "error",
                            "message", "Item not found"
                    ));
                }

            } else {

                List<ItemDTO> items = ITEMBO.getAllItems();
                objectMapper.writeValue(response.getWriter(), items);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "code", "500",
                    "status", "error",
                    "message", "Failed to retrieve item(s)"
            ));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> item = mapper.readValue(req.getInputStream(), Map.class);

        resp.setContentType("application/json");

        ItemDTO itemDTO = new ItemDTO(
                (String) item.get("code"),
                (String) item.get("name"),
                Double.parseDouble(item.get("price").toString()),
                Integer.parseInt(item.get("qty").toString())
        );

        boolean success = false;
        try {
            success = ITEMBO.updateItem(itemDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(success){
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "200",
                    "status", "success",
                    "message", "Item updated successfully"
            ));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "400",
                    "status", "failed",
                    "message", "Item not updated"
            ));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String itemCode = req.getParameter("code");

        resp.setContentType("application/json");

        boolean success = false;
        try {
            success = ITEMBO.deleteItem(itemCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(success){
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "200",
                    "status", "success",
                    "message", "Item deleted successfully"
            ));
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "400",
                    "status", "failed",
                    "message", "Item not deleted"
            ));
        }
    }
}
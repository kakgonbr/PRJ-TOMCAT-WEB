package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCPServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String table = request.getParameter("table");
        String action = request.getParameter("action");
        // int id;
        String idString = request.getParameter("id");

        if (table == null || table.isEmpty()) {

            try {
                request.setAttribute("resources", service.AdminService.DatabaseEditService.getResourceDTOs());
            } catch (java.sql.SQLException e) {
                service.Logging.logger.error("FAILED TO GET DTOS");
            }
            
            request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_CONTROL_PANEL).forward(request, response);

            return;
        }


        if (action == null || action.isEmpty()) {
            service.Logging.logger.warn("ACTION PARAMETER IS MALFORMED OR IS MISSING");

            return;
        }

        try {
            switch (table) {
                case "resources":
                    if (action.equals("edit")) {
                        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT_RESOURCE).forward(request, response);
                        break;
                    }
                    dao.ResourceDAO.deleteMapping(idString);
                break;
                case "products":
                    if (action.equals("edit")) {
                        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT_PRODUCT).forward(request, response);
                        break;
                    }
                    dao.ProductDAO.ProductManager.deleteProduct(Integer.parseInt(idString));
                break;
                case "users":
                    if (action.equals("edit")) {
                        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT_USER).forward(request, response);
                        break;
                    }
                    dao.UserDAO.UserManager.deleteUser(Integer.parseInt(idString)); // can throw here
                break;
                case "shops":
                    if (action.equals("edit")) {
                        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT_SHOP).forward(request, response);
                        break;
                    }
                    dao.ShopDAO.ShopManager.deleteShop(Integer.parseInt(idString)); // can throw here
                break;
                case "promotions":
                    if (action.equals("edit")) {
                        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT_PROMOTION).forward(request, response);
                        break;
                    }
                    // TODO: IMPLEMENT
                break;
            }
        } catch (java.sql.SQLException e) {
            service.Logging.logger.error("ERROR OCCURRED WHILE TRYING TO PERFORM AN ACTION, REASON: {}", e.getMessage());

            return;
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: TEMPORARY CODE, REPLACE LATER
        try {
            model.dto.ResourceDTO resourceDTO = new model.dto.ResourceDTO();
            BeanUtils.populate(resourceDTO, request.getParameterMap());

            service.Logging.logger.info("Persisting resource {}, {}", resourceDTO.getId(), resourceDTO.getSystemPath());

            service.AdminService.DatabaseEditService.persistResourceDTO(resourceDTO);
        } catch (java.sql.SQLException | IllegalAccessException | InvocationTargetException e) {
            service.Logging.logger.error("PERSISTING FAILED, REASON: {}", e.getMessage());
        }
    }
}

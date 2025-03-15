package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import org.apache.commons.beanutils.BeanUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCPServlet extends HttpServlet {
    static {
        // for relationships that can be null, use this to convert empty strings into null keys
        org.apache.commons.beanutils.ConvertUtils.register(new org.apache.commons.beanutils.Converter() {
            @Override
            public Object convert(Class type, Object value) {
                if (value == null || "".equals(value)) {
                    return null;
                }
                return Integer.valueOf(value.toString());
            }
        }, Integer.class);
        
        org.apache.commons.beanutils.ConvertUtils.register(new org.apache.commons.beanutils.Converter() {
            @Override
            public Object convert(Class type, Object value) {
                if (value == null || "".equals(value)) {
                    return null;
                }
                return value;
            }
        }, String.class);

        org.apache.commons.beanutils.ConvertUtils.register(new org.apache.commons.beanutils.Converter() {
            @Override
            public Object convert(Class type, Object value) {
                if (value == null || "".equals(value)) {
                    return null;
                }
                return Long.parseLong(value.toString());
            }
        }, Long.class);
    }
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
        String table = request.getParameter("table");

        if (table == null || table.isEmpty()) {
            // TODO: ADD ERROR REPORT
            response.sendRedirect(request.getServletContext() + "/admin/cp");
            return;
        }

        try {
            service.Logging.logger.info("Trying to persist DTO of table {}", table);

            switch (table) {
                case "resources":
                    model.dto.ResourceDTO resourceDTO = new model.dto.ResourceDTO();
                    BeanUtils.populate(resourceDTO, request.getParameterMap());

                    service.AdminService.DatabaseEditService.persistResourceDTO(resourceDTO);
                break;
                case "shops":
                    model.dto.ShopDTO shopDTO = new model.dto.ShopDTO();
                    BeanUtils.populate(shopDTO, request.getParameterMap());

                    service.AdminService.DatabaseEditService.persistShopDTO(shopDTO);
                break;
                    case "users":
                    model.dto.UserDTO userDTO = new model.dto.UserDTO();
                    BeanUtils.populate(userDTO, request.getParameterMap());

                    service.AdminService.DatabaseEditService.persistUserDTO(userDTO);
                break;
                case "products":
                    model.dto.ProductDTO productDTO = new model.dto.ProductDTO();
                    BeanUtils.populate(productDTO, request.getParameterMap());

                    // service.Logging.logger.info("promo id {}", productDTO.getAvailablePromotionId());

                    service.AdminService.DatabaseEditService.persistProductDTO(productDTO);
                break;
            }
        } catch (java.sql.SQLException | IllegalAccessException | InvocationTargetException e) {
            service.Logging.logger.error("PERSISTING FAILED, REASON: {}", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/cp");
    }
}

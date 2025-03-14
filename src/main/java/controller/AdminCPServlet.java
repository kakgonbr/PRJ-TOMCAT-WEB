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
        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e){
            service.Logging.logger.warn("ID PARAMETER IS MALFORMED OR IS MISSING");

            return;
        }

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

        // TODO: TEMPORARY CODE, REPLACE LATER
        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: TEMPORARY CODE, REPLACE LATER
        try {
            service.AdminService.DatabaseEditService.ResourceDTO resourceDTO = new service.AdminService.DatabaseEditService.ResourceDTO();
            BeanUtils.populate(resourceDTO, request.getParameterMap());
            service.AdminService.DatabaseEditService.persistResourceDTO(resourceDTO);
        } catch (java.sql.SQLException | IllegalAccessException | InvocationTargetException e) {
            service.Logging.logger.error("PERSISTING FAILED, REASON: {}", e.getMessage());
        }
    }
}

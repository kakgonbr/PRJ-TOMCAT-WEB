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

        // TODO: TEMPORARY CODE, REPLACE LATER
        request.getRequestDispatcher(config.Config.JSPMapper.ADMIN_EDIT).forward(request, response);
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

package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resourcePath = request.getPathInfo(); // Does not include this servlet's path, only the part after

        if (resourcePath == null) {
            sendError(response);

            return;
        }

        service.Logging.logger.info("Resource path received: {}", resourcePath);

        try {
            resourcePath = dao.ResourceDAO.getPath(service.DatabaseConnection.getConnection(), resourcePath.substring(1));
        } catch (java.sql.SQLException e) {
            service.Logging.logger.warn("Failed to retrieve the path for resource '{}'. Reason: {}", resourcePath, e.getMessage());
            sendError(response);
            return;
        }

        Path file = Paths.get(config.Config.Resources.ROOT_DIR, resourcePath);
        if (!Files.exists(file) || Files.isDirectory(file)) {
            service.Logging.logger.warn("The resource '{}' could not be found", resourcePath);
            sendError(response);
            return;
        }

        // The mime type tells the browser what kind of file is being sent back
        String mimeType = getServletContext().getMimeType(file.toString());
        response.setContentType(mimeType != null ? mimeType : "application/octet-stream");

        // Stream file content to response
        try (InputStream in = Files.newInputStream(file);
             ServletOutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void sendError(HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
    }
}

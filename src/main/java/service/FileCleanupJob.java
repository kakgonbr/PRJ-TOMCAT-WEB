package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCleanupJob implements Runnable {
    @Override
    public void run() {
        service.Logging.logger.info("Cleanup Job started at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
        
        try (Stream<Path> paths = Files.walk(Paths.get(config.Config.Resources.ROOT_DIR))) {
            java.util.HashSet<String> systemPaths = new java.util.HashSet<>(dao.ResourceDAO.getAllResources().stream().map(model.ResourceMap::getSystemPath).toList());

            service.Logging.logger.info("Files in database: {}", systemPaths.toString());

            java.util.List<Path> filteredPaths = new java.util.ArrayList<>() {
                @Override
                public boolean add(Path e) {
                    if (systemPaths.contains(e.getFileName().toString())) return false;

                    return super.add(e);
                };
            };
            paths.filter(Files::isRegularFile).forEach(filteredPaths::add);
            
            service.Logging.logger.info("Files on the system that need cleanup: {}", filteredPaths.toString());

            // what?????????????????????????????????????
            filteredPaths.forEach(p -> {
                try {
                    java.nio.file.Files.delete(p);
                } catch (IOException e) {
                    service.Logging.logger.warn("Failed to delete file {}, reason: {}", p.getFileName().toString(), e.getMessage());
                }
            });
        } catch (IOException | java.sql.SQLException e) {
            service.Logging.logger.error("FAILED TO CLEAN UP FILES, REASON: {}", e.getMessage());
        }


        service.Logging.logger.info("Cleanup Job completed at: {}", java.time.LocalDateTime.now().format(config.Config.Time.outputFormatTime));
    }
}

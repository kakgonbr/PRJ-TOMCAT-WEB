package service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import jakarta.servlet.http.Part;

public class FileUploadService {
    public static String saveFile(Part filePart, String hashBase) throws java.io.IOException {
        if (hashBase == null || filePart == null) return null;

        String extension = getFileExtension(filePart);

        if (Arrays.asList(config.Config.Resources.ALLOWED_FILE_EXTENSIONS).stream().noneMatch(a -> a.equals(extension))) {
            service.Logging.logger.info("Extension {} is not allowed.", extension);

            return "";
        }

        String hashedFileName = sha256(hashBase) + extension;

        String filePath = config.Config.Resources.ROOT_DIR + "/" + hashedFileName;

        service.Logging.logger.info("Saving file to: {}", filePath);

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        
        return hashedFileName;
    }

    public static String sha256(final String base) {
        try {
            final java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getFileExtension(Part filePart) {
        String fileName = filePart.getSubmittedFileName();
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ""; // Return empty string if no extension
    }
}

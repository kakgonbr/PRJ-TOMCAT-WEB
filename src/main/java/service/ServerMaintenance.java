package service;

public class ServerMaintenance {
    private static boolean isActive;
    private static String reason;

    public static synchronized void enableMaintenance(String t_reason) {
        if (isActive) return;
        reason = t_reason;
        isActive = true;

        service.Logging.logger.warn("SERVER MAINTENANCE ACTIVE, REASON: '{}'", t_reason);
    }

    public static synchronized void disableMaintenance() {
        if (!isActive) return;
        isActive = false;
    }

    public static boolean isActive() {
        return isActive;
    }

    public static String getReason() {
        return reason;
    }
}

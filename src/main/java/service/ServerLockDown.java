package service;

public class ServerLockDown {
    private static boolean isActive;
    private static String reason;

    public static synchronized void lockDownServer(String t_reason) {
        if (isActive) return;
        reason = t_reason;
        isActive = true;

        service.Logging.logger.fatal("SERVER LOCKDOWN ACTIVE, REASON: '{}'", t_reason);
    }

    public static boolean isActive() {
        return isActive;
    }

    public static String getReason() {
        return reason;
    }
}

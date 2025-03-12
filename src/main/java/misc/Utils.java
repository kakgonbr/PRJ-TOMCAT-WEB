package misc;

public class Utils {
    public static String formatDate(java.sql.Date date) {
        return config.Config.Time.outputFormatDate.format(date.toLocalDate());
    }

    public static String formatTime(java.sql.Date date) {
        return config.Config.Time.outputFormatTime.format(date.toLocalDate());
    }

    // public static String formatDate(java.time.LocalDate date) {

    // }

    // public static String formatTime(java.time.LocalDateTime date) {

    // }
}

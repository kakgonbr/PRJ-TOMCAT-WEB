package misc;

public class Utils {
    public static String formatDate(java.util.Date date) {
        return config.Config.Time.outputFormatDate.format(java.time.LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault()));
    }

    public static String formatTime(java.util.Date date) {
        return config.Config.Time.outputFormatTime.format(java.time.LocalDate.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault()));
    }

    // public static String formatDate(java.time.LocalDate date) {

    // }

    // public static String formatTime(java.time.LocalDateTime date) {

    // }
}

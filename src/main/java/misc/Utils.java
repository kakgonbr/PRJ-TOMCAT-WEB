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

    public static class Validator {
        /**
         * Phone number must either start with 0 or \+\d{2}<br></br>
         * Phone number must end with exactly 9 digits
         * @param phoneNumber
         * @return
         */
        public static boolean phoneNumber(String phoneNumber) {
            return java.util.regex.Pattern.compile("^(\\+\\d{2}|0)(\\s|)\\d{9}$").matcher(phoneNumber).find();
        }

        public static boolean email(String email) {
            return java.util.regex.Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$").matcher(email).find();
        }
        
        
        /**
         * Password must contain 1 number (0-9)<br></br>
         * Password must contain 1 uppercase letters<br></br>
         * Password must contain 1 lowercase letters<br></br>
         * Password must contain 1 non-alpha numeric number<br></br>
         * Password is 8-16 characters with no space
         * @param password
         * @return
         */
        public static boolean password(String password) {
            return java.util.regex.Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$").matcher(password).find();
        }

        /**
         * Username can ONLY contain a-z, A-Z, 0-9, "-" and "_"<br></br>
         * Username cannot be shorter than 8 or exceed 32 characters
         * @param username
         * @return
         */
        public static boolean username(String username) {
            return java.util.regex.Pattern.compile("^[\\w_-]{8,32}$", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(username).find();
        }
    }
}

package seedu.address.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {
    public static boolean containsIgnoreCase(String source, String query) {
        String[] split = source.toLowerCase().split("\\s+");
        List<String> strings = Arrays.asList(split);
        return strings.stream().filter(s -> s.equals(query.toLowerCase())).count() > 0;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        assert t != null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if s represents an unsigned integer e.g. 1, 2, 3, ... <br>
     *   Will return false for null, empty string, "-1", "0", "+1", and " 2 " (untrimmed) "3 0" (contains whitespace).
     * @param s Should be trimmed.
     */
    public static boolean isUnsignedInteger(String s) {
        return s != null && s.matches("^0*[1-9]\\d*$");
    }

    /**
     * returns a neater version of Local Date Time to string
     * @param localDateTime
     * @return local date time in dd/mm/yyyy Time: HH:mm (24hr)
     */
    public static String localDateTimeToPrettyString(LocalDateTime ldt) {
        String ldtString = ldt.toString();
        String[] parts = ldtString.split("-");
        String yyyy = parts[0];
        String mm = parts[1];
        ldtString = parts[2];
        parts = ldtString.split("T");
        String dd = parts[0];
        String time = parts[1];

        String pretty = dd + "/" + mm + "/" + yyyy + " " + "Time: " + time;

        return pretty;
    }
}

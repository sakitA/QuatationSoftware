
package quotationsoftware.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Utility class for specific data format definitions.
 */
public abstract class Formats {
    
    public static final String pattern = "dd/MM/yy";
    public static final DateFormat dateFormat = new SimpleDateFormat(pattern);
    
    /**
     * Returns a formatted string for the given date.
     * @param date
     * @return 
     */
    public static String toDateFormat(Date date) {
        if (date == null)
            return "N/A";
        return dateFormat.format(date);
    }    
}

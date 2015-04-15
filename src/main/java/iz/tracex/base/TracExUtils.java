package iz.tracex.base;

import org.joda.time.DateTime;

/**
 *
 * @author izumi_j
 *
 */
public final class TracExUtils {
    private TracExUtils() {
    }

    public static String unixTimeToDateStr(long time) {
        final long mills = time * 1000;
        return new DateTime(mills).toString("yyyy/MM/dd");
    }

    public static String unixTimeToDateTimeStr(long time) {
        final long mills = time * 1000;
        return new DateTime(mills).toString("yyyy/MM/dd HH:mm:ss");
    }

    public static long millsToUnixTime(long mills) {
        return mills / 1000;
    }

    public static long nowAsUnixTime() {
        return DateTime.now().getMillis() / 1000;
    }
}

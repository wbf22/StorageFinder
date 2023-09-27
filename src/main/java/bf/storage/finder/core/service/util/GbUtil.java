package bf.storage.finder.core.service.util;

public class GbUtil {
    public static float getGB(long l) {
        return ((float) l)/ 1000000000;
    }

    public static String getGBAsString(long l) {
        return String.format("%.1f", getGB(l)) + " Gb";
    }

    public static long getBytes(float gb) {
        return (long) (gb * 1000000000);
    }
}

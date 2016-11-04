package saschpe.poker.util;

import java.util.Arrays;
import java.util.List;

public final class PlanningPoker {
    public static final List<String> FIBONACCI_LIST = Arrays.asList("0", "½", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "☕");
    public static final int FIBONACCI_POSITION = 5;
    public static final List<String> T_SHIRT_SIZE_LIST = Arrays.asList("XS", "S", "M", "L", "XL", "XXL");
    public static final int T_SHIRT_SIZE_POSITION = 2;

    public enum Flavor {
        FIBONACCI,
        T_SHIRT_SIZES
    }
}

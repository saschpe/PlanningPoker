package saschpe.poker.util;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;

import java.util.Arrays;
import java.util.List;

public final class PlanningPoker {
    public static final int FIBONACCI = 1;
    public static final int T_SHIRT_SIZES = 2;
    public static final int IDEAL_DAYS = 3;
    @IntDef({FIBONACCI, T_SHIRT_SIZES, IDEAL_DAYS})
    public @interface Flavor {}

    // Use an "@" for coffee, because "☕" doesn't render correctly on most devices
    public static final List<String> FIBONACCI_VALUES = Arrays.asList("0", "½", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "@");
    public static final List<String> T_SHIRT_SIZE_VALUES = Arrays.asList("XS", "S", "M", "L", "XL", "XXL");
    public static final List<String> IDEAL_DAYS_VALUES = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+");

    public static final int FIBONACCI_DEFAULT = 5;
    public static final int T_SHIRT_SIZE_DEFAULT = 2;
    public static final int IDEAL_DAYS_DEFAULT = 5;
    @SuppressLint("UniqueConstants")
    @IntDef({FIBONACCI_DEFAULT, T_SHIRT_SIZE_DEFAULT, IDEAL_DAYS_DEFAULT})
    public @interface FlavorDefault {}
}


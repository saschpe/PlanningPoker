package saschpe.poker.util;

import android.support.annotation.IntDef;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseIntArray;

import java.util.Arrays;
import java.util.List;

public final class PlanningPoker {
    public static final int FIBONACCI = 1;
    public static final int T_SHIRT_SIZES = 2;
    public static final int IDEAL_DAYS = 3;
    @IntDef({FIBONACCI, T_SHIRT_SIZES, IDEAL_DAYS})
    public @interface Flavor {}

    public static final SimpleArrayMap<Integer, List<String>> VALUES = new SimpleArrayMap<>(3);
    static {
        // Use an "@" for coffee, because "☕" doesn't render correctly on most devices
        VALUES.put(FIBONACCI, Arrays.asList("0", "½", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "@"));
        VALUES.put(T_SHIRT_SIZES, Arrays.asList("XS", "S", "M", "L", "XL", "XXL"));
        VALUES.put(IDEAL_DAYS, Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"));
    }

    public static final SparseIntArray DEFAULTS = new SparseIntArray(3);
    static {
        DEFAULTS.append(FIBONACCI, 5);
        DEFAULTS.append(T_SHIRT_SIZES, 2);
        DEFAULTS.append(IDEAL_DAYS, 5);
    }
}


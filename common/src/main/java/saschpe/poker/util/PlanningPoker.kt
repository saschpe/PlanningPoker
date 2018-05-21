/*
 * Copyright 2016 Sascha Peilicke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package saschpe.poker.util

import android.support.annotation.IntDef
import android.support.v4.util.SimpleArrayMap
import android.util.SparseIntArray
import java.util.*

object PlanningPoker {
    const val FIBONACCI = 1
    const val T_SHIRT_SIZES = 2
    const val IDEAL_DAYS = 3

    val values = SimpleArrayMap<Int, List<String>>(3)
    val defaults = SparseIntArray(3)

    @IntDef(FIBONACCI, T_SHIRT_SIZES, IDEAL_DAYS)
    annotation class Flavor

    init {
        // Use an "@" for coffee, because "☕" doesn't render correctly on most devices
        values.put(FIBONACCI, Arrays.asList("0", "½", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "@"))
        values.put(T_SHIRT_SIZES, Arrays.asList("XS", "S", "M", "L", "XL", "XXL"))
        values.put(IDEAL_DAYS, Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"))
    }

    init {
        defaults.append(FIBONACCI, 5)
        defaults.append(T_SHIRT_SIZES, 2)
        defaults.append(IDEAL_DAYS, 5)
    }
}


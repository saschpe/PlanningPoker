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

import android.util.SparseIntArray
import androidx.annotation.IntDef

object PlanningPoker {
    const val FIBONACCI = 1
    const val T_SHIRT_SIZES = 2
    const val IDEAL_DAYS = 3
    const val POWERS_OF_TWO = 4
    const val REAL_FIBONACCI = 5

    val values = androidx.collection.SimpleArrayMap<Int, List<String>>(3)
    val defaults = SparseIntArray(5)

    @IntDef(FIBONACCI, T_SHIRT_SIZES, IDEAL_DAYS, POWERS_OF_TWO)
    annotation class Flavor

    init {
        // Use an "@" for coffee, because "☕" doesn't render correctly on most devices
        values.put(FIBONACCI, listOf("0", "½", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "@"))
        values.put(T_SHIRT_SIZES, listOf("XS", "S", "M", "L", "XL", "XXL"))
        values.put(IDEAL_DAYS, listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"))
        values.put(POWERS_OF_TWO, listOf("0", "1", "2", "4", "8", "16", "32", "64"))
        values.put(REAL_FIBONACCI, listOf("0", "1", "2", "3", "5", "8", "13", "21", "34", "55", "89", "144", "?", "☕"))
    }

    init {
        defaults.append(FIBONACCI, 5)
        defaults.append(T_SHIRT_SIZES, 2)
        defaults.append(IDEAL_DAYS, 5)
        defaults.append(POWERS_OF_TWO, 2)
        defaults.append(REAL_FIBONACCI, 5)
    }
}


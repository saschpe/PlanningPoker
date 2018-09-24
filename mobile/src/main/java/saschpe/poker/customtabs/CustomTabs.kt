/*
 * Copyright 2018 Sascha Peilicke
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

package saschpe.poker.customtabs

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import saschpe.poker.R
import saschpe.poker.util.BitmapHelper

object CustomTabs {
    private const val PRIVACY_POLICY_URL =
        "https://sites.google.com/view/planningpoker/privacy-policy"

    /**
     * Start Privacy Policy custom tab.
     *
     * See https://developer.chrome.com/multidevice/android/customtabs
     *
     * @param context Activity context
     */
    fun startPrivacyPolicy(context: Context) {
        startUrl(context, PRIVACY_POLICY_URL)
    }

    fun startUrl(context: Context, url: String, animate: Boolean = false) {
        val customTabsIntentBuilder = getDefaultCustomTabsIntentBuilder(context)
        if (animate) {
            customTabsIntentBuilder
                .setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)
        }
        val customTabsIntent = customTabsIntentBuilder.build()

        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(
            context, customTabsIntent, Uri.parse(url), WebViewFallback()
        )
    }

    private fun getDefaultCustomTabsIntentBuilder(context: Context): CustomTabsIntent.Builder {
        val builder = CustomTabsIntent.Builder()
            .addDefaultShareMenuItem()
            .setToolbarColor(ResourcesCompat.getColor(context.resources, R.color.primary, null))
            .setShowTitle(true)

        val backArrow = BitmapHelper.getBitmapFromVectorDrawable(context, R.drawable.ic_arrow_back)
        if (backArrow != null) {
            builder.setCloseButtonIcon(backArrow)
        }
        return builder
    }
}

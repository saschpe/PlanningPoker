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

package saschpe.poker.activity

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_info.*
import saschpe.android.versioninfo.VersionInfoUtils
import saschpe.poker.BuildConfig
import saschpe.poker.R

class InfoActivity : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        copyright.text = resources.getString(R.string.copyright_text_template,
                VersionInfoUtils.getFormattedVersion(this, packageName,
                        BuildConfig.VERSION_NAME),
                VersionInfoUtils.getFormattedCopyright(this, packageName,
                        "Sascha Peilicke"))
    }
}
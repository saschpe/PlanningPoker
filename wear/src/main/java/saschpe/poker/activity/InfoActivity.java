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

package saschpe.poker.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;

import saschpe.android.versioninfo.VersionInfoUtils;
import saschpe.poker.BuildConfig;
import saschpe.poker.R;

public class InfoActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        String description = VersionInfoUtils.getFormattedVersion(this, getPackageName(), BuildConfig.VERSION_NAME) + "\n" + VersionInfoUtils.getFormattedCopyright(this, getPackageName(), "Sascha Peilicke");

        CardFragment cardFragment = CardFragment.create(
                getString(R.string.app_name), description);

        getFragmentManager().beginTransaction()
                .add(R.id.frame_layout, cardFragment)
                .commit();
    }
}
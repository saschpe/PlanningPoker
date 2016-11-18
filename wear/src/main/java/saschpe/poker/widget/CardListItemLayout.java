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

package saschpe.poker.widget;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import saschpe.poker.R;

public class CardListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    private TextView center;
    private final float fadedTextAlpha;

    public CardListItemLayout(Context context) {
        this(context, null);
    }

    public CardListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fadedTextAlpha = 0.5f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        center = (TextView) findViewById(R.id.center);
    }

    @Override
    public void onCenterPosition(boolean b) {
        center.setAlpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        center.setAlpha(fadedTextAlpha);
    }
}

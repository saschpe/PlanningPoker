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

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.drawer.WearableActionDrawer;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import saschpe.android.utils.widget.SpacesItemDecoration;
import saschpe.poker.R;
import saschpe.poker.adapter.WearCardArrayAdapter;
import saschpe.poker.util.PlanningPoker;

public final class MainActivity extends WearableActivity implements
        WearableActionDrawer.OnMenuItemClickListener {
    private static final String PREFS_FLAVOR = "flavor2";
    private static final String STATE_FLAVOR = "flavor";
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private @PlanningPoker.Flavor int flavor;
    private WearCardArrayAdapter arrayAdapter;
    private WearableActionDrawer actionDrawer;
    private WearableDrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private TextView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        if (savedInstanceState != null) {
            //noinspection WrongConstant
            flavor = savedInstanceState.getInt(STATE_FLAVOR, PlanningPoker.FIBONACCI);
        } else {
            // Either load flavor from previous invocation or use default
            //noinspection WrongConstant
            flavor = PreferenceManager.getDefaultSharedPreferences(this)
                    .getInt(PREFS_FLAVOR, PlanningPoker.FIBONACCI);
        }

        clock = findViewById(R.id.clock);

        // Compute spacing between cards
        float marginDp = getResources().getDimension(R.dimen.activity_horizontal_margin) / 8;
        int spacePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp, getResources().getDisplayMetrics());

        // Setup recycler
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacePx, layoutManager.getOrientation()));
        arrayAdapter = new WearCardArrayAdapter(this, PlanningPoker.INSTANCE.getValues().get(flavor), WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE);
        recyclerView.setAdapter(arrayAdapter);
        recyclerView.scrollToPosition(PlanningPoker.INSTANCE.getDefaults().get(flavor));

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Main Wearable Drawer Layout that wraps all content
        drawerLayout = findViewById(R.id.drawer_layout);
        // Bottom Action Drawer
        actionDrawer = findViewById(R.id.bottom_action_drawer);
        // Populate Action Drawer Menu
        Menu menu = actionDrawer.getMenu();
        getMenuInflater().inflate(R.menu.action_drawer, menu);
        switch (flavor) {
            case PlanningPoker.FIBONACCI:
                menu.findItem(R.id.fibonacci).setChecked(true);
                break;
            case PlanningPoker.T_SHIRT_SIZES:
                menu.findItem(R.id.t_shirt_sizes).setChecked(true);
                break;
            case PlanningPoker.IDEAL_DAYS:
                menu.findItem(R.id.ideal_days).setChecked(true);
                break;
        }
        actionDrawer.setOnMenuItemClickListener(this);
        // Peeks action drawer on the bottom.
        drawerLayout.peekDrawer(Gravity.BOTTOM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Persist current flavor for next invocation
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt(PREFS_FLAVOR, flavor)
                .apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current flavor over configuration change
        outState.putInt(STATE_FLAVOR, flavor);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fibonacci:
                updateFlavor(PlanningPoker.FIBONACCI);
                item.setChecked(true);
                break;
            case R.id.t_shirt_sizes:
                updateFlavor(PlanningPoker.T_SHIRT_SIZES);
                item.setChecked(true);
                break;
            case R.id.ideal_days:
                updateFlavor(PlanningPoker.IDEAL_DAYS);
                item.setChecked(true);
                break;
            case R.id.version_info:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }
        actionDrawer.closeDrawer();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateFlavor(@PlanningPoker.Flavor int flavor) {
        this.flavor = flavor;
        arrayAdapter.replaceAll(PlanningPoker.INSTANCE.getValues().get(flavor));
        recyclerView.scrollToPosition(PlanningPoker.INSTANCE.getDefaults().get(flavor));
    }

    private void updateDisplay() {
        if (isAmbient()) {
            arrayAdapter.setViewType(WearCardArrayAdapter.DARK_CARD_VIEW_TYPE);
            clock.setText(AMBIENT_DATE_FORMAT.format(new Date()));
            clock.setVisibility(View.VISIBLE);
            drawerLayout.closeDrawer(Gravity.BOTTOM);
        } else {
            arrayAdapter.setViewType(WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE);
            clock.setVisibility(View.GONE);
        }
    }
}

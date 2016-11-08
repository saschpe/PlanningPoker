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

import saschpe.poker.R;
import saschpe.poker.adapter.WearCardArrayAdapter;
import saschpe.poker.util.PlanningPoker;
import saschpe.poker.widget.recycler.SpacesItemDecoration;

public class MainActivity extends WearableActivity implements
        WearableActionDrawer.OnMenuItemClickListener {
    private static final String PREFS_FLAVOR = "flavor";
    private static final String STATE_FLAVOR = "flavor";
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private PlanningPoker.Flavor flavor;
    private WearCardArrayAdapter arrayAdapter;
    private WearableActionDrawer actionDrawer;
    private RecyclerView recyclerView;
    private TextView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        if (savedInstanceState != null) {
            flavor = (PlanningPoker.Flavor) savedInstanceState.getSerializable(STATE_FLAVOR);
        } else {
            // Either load flavor from previous invocation or use default
            String flavorString = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(PREFS_FLAVOR, PlanningPoker.Flavor.FIBONACCI.toString());

            flavor = PlanningPoker.Flavor.fromString(flavorString);
        }

        clock = (TextView) findViewById(R.id.clock);

        // Compute spacing between cards
        float marginDp = getResources().getDimension(R.dimen.activity_horizontal_margin) / 8;
        int spacePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp, getResources().getDisplayMetrics());

        // Setup recycler
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacePx, layoutManager.getOrientation()));
        updateFlavor();
        recyclerView.setAdapter(arrayAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Main Wearable Drawer Layout that wraps all content
        WearableDrawerLayout drawerLayout = (WearableDrawerLayout) findViewById(R.id.drawer_layout);

        // Bottom Action Drawer
        actionDrawer = (WearableActionDrawer) findViewById(R.id.bottom_action_drawer);
        // Populate Action Drawer Menu
        Menu menu = actionDrawer.getMenu();
        getMenuInflater().inflate(R.menu.action_drawer, menu);
        switch (flavor) {
            case FIBONACCI:
                menu.findItem(R.id.fibonacci).setChecked(true);
                break;
            case T_SHIRT_SIZES:
                menu.findItem(R.id.t_shirt_sizes).setChecked(true);
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
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(PREFS_FLAVOR, flavor.toString())
                .apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_FLAVOR, flavor);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fibonacci:
                flavor = PlanningPoker.Flavor.FIBONACCI;
                updateFlavor();
                item.setChecked(true);
                break;
            case R.id.t_shirt_sizes:
                flavor = PlanningPoker.Flavor.T_SHIRT_SIZES;
                updateFlavor();
                item.setChecked(true);
                break;
            case R.id.version_info:
                startActivity(new Intent(this, InfoActivity.class));
                break;
        }
        actionDrawer.closeDrawer();
        return super.onOptionsItemSelected(item);
    }

    private void updateDisplay() {
        if (isAmbient()) {
            arrayAdapter.setViewType(WearCardArrayAdapter.DARK_CARD_VIEW_TYPE);
            clock.setText(AMBIENT_DATE_FORMAT.format(new Date()));
            clock.setVisibility(View.VISIBLE);
        } else {
            arrayAdapter.setViewType(WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE);
            clock.setVisibility(View.GONE);
        }
    }

    private void updateFlavor() {
        switch (flavor) {
            case FIBONACCI:
                if (arrayAdapter == null) {
                    arrayAdapter = new WearCardArrayAdapter(this, PlanningPoker.FIBONACCI_LIST, WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE);
                } else {
                    arrayAdapter.replaceAll(PlanningPoker.FIBONACCI_LIST);
                }
                recyclerView.scrollToPosition(PlanningPoker.FIBONACCI_POSITION);
                break;
            case T_SHIRT_SIZES:
                if (arrayAdapter == null) {
                    arrayAdapter = new WearCardArrayAdapter(this, PlanningPoker.T_SHIRT_SIZE_LIST, WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE);
                } else {
                    arrayAdapter.replaceAll(PlanningPoker.T_SHIRT_SIZE_LIST);
                }
                recyclerView.scrollToPosition(PlanningPoker.T_SHIRT_SIZE_POSITION);
                break;
        }
    }
}

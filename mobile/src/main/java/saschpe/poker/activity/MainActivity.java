package saschpe.poker.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import saschpe.poker.BuildConfig;
import saschpe.poker.R;
import saschpe.poker.adapter.CardArrayAdapter;
import saschpe.poker.util.PlanningPoker;
import saschpe.poker.widget.recycler.SpacesItemDecoration;
import saschpe.versioninfo.widget.VersionInfoDialogFragment;

public final class MainActivity extends AppCompatActivity {
    private static final String PREFS_FLAVOR = "flavor";
    private static final String STATE_FLAVOR = "flavor";

    private CardArrayAdapter arrayAdapter;
    private PlanningPoker.Flavor flavor;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            flavor = (PlanningPoker.Flavor) savedInstanceState.getSerializable(STATE_FLAVOR);
        } else {
            // Either load flavor from previous invocation or use default
            String flavorString = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(PREFS_FLAVOR, PlanningPoker.Flavor.FIBONACCI.toString());

            flavor = PlanningPoker.Flavor.fromString(flavorString);
        }

        // Compute spacing between cards
        float marginDp = 8;
        int spacePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp, getResources().getDisplayMetrics());

        // Setup recycler
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacePx, layoutManager.getOrientation()));
        updateFlavor();
        recyclerView.setAdapter(arrayAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Persist current flavor for next invocation
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(PREFS_FLAVOR, flavor.toString())
                .apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        switch (flavor) {
            case FIBONACCI:
                menu.findItem(R.id.fibonacci).setChecked(true);
                break;
            case T_SHIRT_SIZES:
                menu.findItem(R.id.t_shirt_sizes).setChecked(true);
                break;
            case IDEAL_DAYS:
                menu.findItem(R.id.ideal_days).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.ideal_days:
                flavor = PlanningPoker.Flavor.IDEAL_DAYS;
                updateFlavor();
                item.setChecked(true);
                break;
            case R.id.version_info:
                VersionInfoDialogFragment
                        .newInstance(
                                getString(R.string.app_name),
                                BuildConfig.VERSION_NAME,
                                "Sascha Peilicke",
                                R.mipmap.ic_launcher)
                        .show(getFragmentManager(), "version_info");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_FLAVOR, flavor);
    }

    private void updateFlavor() {
        List<String> values;
        int position;
        switch (flavor) {
            case FIBONACCI:
            default:
                values = PlanningPoker.FIBONACCI_LIST;
                position = PlanningPoker.FIBONACCI_POSITION;
                break;
            case T_SHIRT_SIZES:
                values = PlanningPoker.T_SHIRT_SIZE_LIST;
                position = PlanningPoker.T_SHIRT_SIZE_POSITION;
                break;
            case IDEAL_DAYS:
                values = PlanningPoker.IDEAL_DAYS_LIST;
                position = PlanningPoker.IDEAL_DAYS_POSITION;
                break;
        }
        if (arrayAdapter == null) {
            arrayAdapter = new CardArrayAdapter(this, values, CardArrayAdapter.BIG_CARD_VIEW_TYPE, position);
        } else {
            arrayAdapter.replaceAll(values);
        }
        recyclerView.scrollToPosition(position);
    }
}

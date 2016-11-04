package saschpe.poker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Menu;
import android.view.MenuItem;

import saschpe.poker.BuildConfig;
import saschpe.poker.R;
import saschpe.poker.adapter.CardArrayAdapter;
import saschpe.poker.util.PlanningPoker;
import saschpe.versioninfo.widget.VersionInfoDialogFragment;

public final class MainActivity extends AppCompatActivity {
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
            flavor = PlanningPoker.Flavor.FIBONACCI;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        updateFlavor();
        recyclerView.setAdapter(arrayAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
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
            case R.id.action_version_info:
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
        switch (flavor) {
            case FIBONACCI:
                if (arrayAdapter == null) {
                    arrayAdapter = new CardArrayAdapter(this, PlanningPoker.FIBONACCI_LIST);
                } else {
                    arrayAdapter.replace(PlanningPoker.FIBONACCI_LIST);
                }
                recyclerView.scrollToPosition(PlanningPoker.FIBONACCI_POSITION);
                break;
            case T_SHIRT_SIZES:
                if (arrayAdapter == null) {
                    arrayAdapter = new CardArrayAdapter(this, PlanningPoker.T_SHIRT_SIZE_LIST);
                } else {
                    arrayAdapter.replace(PlanningPoker.T_SHIRT_SIZE_LIST);
                }
                recyclerView.scrollToPosition(PlanningPoker.T_SHIRT_SIZE_POSITION);
                break;
        }
    }
}

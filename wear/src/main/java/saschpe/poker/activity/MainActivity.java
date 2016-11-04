package saschpe.poker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableRecyclerView;

import java.util.Arrays;
import java.util.List;

import saschpe.poker.R;
import saschpe.poker.adapter.CardArrayAdapter;

public class MainActivity extends Activity {
    public static final List<String> PLANNING_POKER_VALUES = Arrays.asList("0", "0xC2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "0xE2");
    private WearableRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardArrayAdapter arrayAdapter = new CardArrayAdapter(PLANNING_POKER_VALUES);

        recyclerView = (WearableRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setCircularScrollingGestureEnabled(true);
        recyclerView.setBezelWidth(0.5f);
        recyclerView.setScrollDegreesPerScreen(90);
        recyclerView.setAdapter(arrayAdapter);
    }
}

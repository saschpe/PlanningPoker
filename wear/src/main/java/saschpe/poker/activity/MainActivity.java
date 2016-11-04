package saschpe.poker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import saschpe.poker.R;
import saschpe.poker.adapter.CardArrayAdapter;
import saschpe.poker.util.Constants;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardArrayAdapter arrayAdapter = new CardArrayAdapter(Constants.FIBONACCI_LIST);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setCircularScrollingGestureEnabled(true);
        //recyclerView.setBezelWidth(0.5f);
        //recyclerView.setScrollDegreesPerScreen(90);
        recyclerView.setAdapter(arrayAdapter);
    }
}

package saschpe.poker.view;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import saschpe.poker.R;

public class PokerCardListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    private TextView center;
    private final float fadedTextAlpha;

    public PokerCardListItemLayout(Context context) {
        this(context, null);
    }

    public PokerCardListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PokerCardListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

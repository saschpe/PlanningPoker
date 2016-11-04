package saschpe.poker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import saschpe.poker.R;
import saschpe.poker.adapter.base.WearableArrayAdapter;

public class CardArrayAdapter extends WearableArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
    private final LayoutInflater inflater;

    public CardArrayAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(inflater.inflate(R.layout.view_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.center.setText(getItem(position));
    }

    static final class CardViewHolder extends WearableListView.ViewHolder {
        TextView center;

        CardViewHolder(View itemView) {
            super(itemView);
            center = (TextView) itemView.findViewById(R.id.center);
        }
    }
}

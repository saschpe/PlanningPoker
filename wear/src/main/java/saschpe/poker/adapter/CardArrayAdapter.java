package saschpe.poker.adapter;

import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import saschpe.poker.wear.R;
import saschpe.poker.adapter.base.ArrayAdapter;

public class CardArrayAdapter extends ArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
    public CardArrayAdapter(List<String> objects) {
        super(objects);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.value.setText(getItem(position));
    }

    static final class CardViewHolder extends WearableListView.ViewHolder {
        ImageView left;
        TextView value;
        ImageView right;

        CardViewHolder(View itemView) {
            super(itemView);
            left = (ImageView) itemView.findViewById(R.id.left);
            value = (TextView) itemView.findViewById(R.id.value);
            right = (ImageView) itemView.findViewById(R.id.right);
        }
    }
}

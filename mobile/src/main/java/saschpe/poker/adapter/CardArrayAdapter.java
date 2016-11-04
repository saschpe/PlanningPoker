package saschpe.poker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import saschpe.poker.R;
import saschpe.poker.adapter.base.ArrayAdapter;

public final class CardArrayAdapter extends ArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
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
        holder.bottomLeft.setText(getItem(position));
        holder.center.setText(getItem(position));
        holder.topRight.setText(getItem(position));
    }

    static final class CardViewHolder extends RecyclerView.ViewHolder {
        TextView bottomLeft;
        TextView center;
        TextView topRight;

        CardViewHolder(View itemView) {
            super(itemView);
            bottomLeft = (TextView) itemView.findViewById(R.id.bottomLeft);
            center = (TextView) itemView.findViewById(R.id.center);
            topRight = (TextView) itemView.findViewById(R.id.topRight);
        }
    }
}

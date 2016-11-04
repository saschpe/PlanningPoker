package saschpe.poker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import saschpe.poker.R;
import saschpe.poker.adapter.base.ArrayAdapter;

public class CardArrayAdapter extends ArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
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

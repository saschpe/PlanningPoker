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

public final class WearCardArrayAdapter extends ArrayAdapter<String, WearCardArrayAdapter.WearCardViewHolder> {
    private final LayoutInflater inflater;

    public WearCardArrayAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(objects);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public WearCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearCardViewHolder(inflater.inflate(R.layout.view_wear_card, parent, false));
    }

    @Override
    public void onBindViewHolder(WearCardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static final class WearCardViewHolder extends RecyclerView.ViewHolder {
        private TextView center;

        WearCardViewHolder(View itemView) {
            super(itemView);
            center = (TextView) itemView.findViewById(R.id.center);
        }

        void bind(String item) {
            center.setText(item);
        }
    }
}

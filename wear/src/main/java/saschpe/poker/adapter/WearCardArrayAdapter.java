package saschpe.poker.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
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
    public static final int LIGHT_CARD_VIEW_TYPE = 1;
    public static final int DARK_CARD_VIEW_TYPE = 2;

    @IntDef({LIGHT_CARD_VIEW_TYPE, DARK_CARD_VIEW_TYPE})
    @interface ViewType {}

    private @ViewType int viewType;
    private final LayoutInflater inflater;

    public WearCardArrayAdapter(@NonNull Context context, @NonNull List<String> objects, @ViewType int viewType) {
        super(objects);
        inflater = LayoutInflater.from(context);
        this.viewType = viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public WearCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LIGHT_CARD_VIEW_TYPE:
            default:
                return new LightCardViewHolder(inflater.inflate(R.layout.view_light_card, parent, false));
            case DARK_CARD_VIEW_TYPE:
                return new DarkCardViewHolder(inflater.inflate(R.layout.view_dark_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(WearCardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static abstract class WearCardViewHolder extends RecyclerView.ViewHolder {
        private TextView center;

        WearCardViewHolder(View itemView) {
            super(itemView);
            center = (TextView) itemView.findViewById(R.id.center);
        }

        void bind(String item) {
            center.setText(item);
        }
    }

    static final class LightCardViewHolder extends WearCardViewHolder {
        LightCardViewHolder(View itemView) {
            super(itemView);
        }
    }

    static final class DarkCardViewHolder extends WearCardViewHolder {
        DarkCardViewHolder(View itemView) {
            super(itemView);
        }
    }
}

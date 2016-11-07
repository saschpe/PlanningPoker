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

public final class CardArrayAdapter extends ArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
    public static final int BIG_CARD_VIEW_TYPE = 1;
    public static final int SMALL_CARD_VIEW_TYPE = 2;

    @IntDef({BIG_CARD_VIEW_TYPE, SMALL_CARD_VIEW_TYPE})
    @interface ViewType {}

    private @ViewType int viewType;
    private final LayoutInflater inflater;

    public CardArrayAdapter(@NonNull Context context, @NonNull List<String> objects, @ViewType int viewType) {
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
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SMALL_CARD_VIEW_TYPE:
                return new SmallCardViewHolder(inflater.inflate(R.layout.view_small_card, parent, false));
            case BIG_CARD_VIEW_TYPE:
            default:
                return new BigCardViewHolder(inflater.inflate(R.layout.view_big_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static abstract class CardViewHolder extends RecyclerView.ViewHolder {
        CardViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(String item);
    }

    private static final class SmallCardViewHolder extends CardViewHolder {
        private TextView center;

        SmallCardViewHolder(View itemView) {
            super(itemView);
            center = (TextView) itemView.findViewById(R.id.center);
        }

        void bind(String item) {
            center.setText(item);
        }
    }

    private static final class BigCardViewHolder extends CardViewHolder {
        private TextView bottomLeft;
        private TextView center;
        private TextView topRight;

        BigCardViewHolder(View itemView) {
            super(itemView);
            bottomLeft = (TextView) itemView.findViewById(R.id.bottomLeft);
            center = (TextView) itemView.findViewById(R.id.center);
            topRight = (TextView) itemView.findViewById(R.id.topRight);
        }

        void bind(String item) {
            bottomLeft.setText(item);
            center.setText(item);
            topRight.setText(item);
        }
    }
}

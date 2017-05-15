/*
 * Copyright 2016 Sascha Peilicke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package saschpe.poker.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import saschpe.android.utils.adapter.base.ArrayAdapter;
import saschpe.poker.R;

public final class CardArrayAdapter extends ArrayAdapter<String, CardArrayAdapter.CardViewHolder> {
    public static final int BIG_CARD_VIEW_TYPE = 1;
    public static final int BIG_BLACK_CARD_VIEW_TYPE = 2;
    public static final int SMALL_CARD_VIEW_TYPE = 3;
    private static final int HELP_CARD_VIEW_TYPE = 4;
    private static final String PREF_HELP_DISMISSED = "help_dismissed";

    @IntDef({BIG_CARD_VIEW_TYPE, BIG_BLACK_CARD_VIEW_TYPE, SMALL_CARD_VIEW_TYPE})
    @interface ViewType {}

    public interface OnSmallCardClickListener {
        void onCardClick(int position);
    }

    private @ViewType int viewType;
    private final LayoutInflater inflater;
    private final SharedPreferences prefs;
    private boolean helpDismissed;
    private final int helpViewPosition;
    private OnSmallCardClickListener onSmallCardClickListener;

    public CardArrayAdapter(@NonNull Context context, @NonNull List<String> objects, @ViewType int viewType, int helpViewPosition) {
        super(objects);
        inflater = LayoutInflater.from(context);
        this.viewType = viewType;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        helpDismissed = prefs.getBoolean(PREF_HELP_DISMISSED, false);
        this.helpViewPosition = helpViewPosition;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public void setOnSmallCardClickListener(OnSmallCardClickListener onSmallCardClickListener) {
        this.onSmallCardClickListener = onSmallCardClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (!helpDismissed && position == helpViewPosition) {
            return HELP_CARD_VIEW_TYPE;
        } else {
            return viewType;
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HELP_CARD_VIEW_TYPE:
                return new HelpCardViewHolder(inflater.inflate(R.layout.view_help_card, parent, false));
            case SMALL_CARD_VIEW_TYPE:
                return new SmallCardViewHolder(inflater.inflate(R.layout.view_small_card, parent, false));
            case BIG_CARD_VIEW_TYPE:
            default:
                return new BigCardViewHolder(inflater.inflate(R.layout.view_big_card, parent, false));
            case BIG_BLACK_CARD_VIEW_TYPE:
                return new BigBlackCardViewHolder(inflater.inflate(R.layout.view_big_black_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, int position) {
        if (!helpDismissed && position == helpViewPosition) {
            HelpCardViewHolder helpCardViewHolder = (HelpCardViewHolder) holder;
            helpCardViewHolder.dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helpDismissed = true;
                    notifyItemRemoved(helpViewPosition);
                    prefs.edit().putBoolean(PREF_HELP_DISMISSED, true).apply();
                }
            });
        } else {
            if (!helpDismissed && position > helpViewPosition) {
                position -= 1;  // Account for visible help card.
            }

            switch (viewType) {
                case SMALL_CARD_VIEW_TYPE:
                    SmallCardViewHolder smallCardViewHolder = (SmallCardViewHolder) holder;
                    smallCardViewHolder.center.setText(getItem(position));
                    smallCardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onSmallCardClickListener != null) {
                                onSmallCardClickListener.onCardClick(holder.getAdapterPosition());
                            }
                        }
                    });
                    break;
                case BIG_BLACK_CARD_VIEW_TYPE:
                    break;
                case BIG_CARD_VIEW_TYPE:
                default:
                    BigCardViewHolder bigCardViewHolder = (BigCardViewHolder) holder;
                    bigCardViewHolder.bottomLeft.setText(getItem(position));
                    bigCardViewHolder.center.setText(getItem(position));
                    bigCardViewHolder.topRight.setText(getItem(position));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (!helpDismissed) {
            count += 1; // Account for visible help card
        }
        return count;
    }

    static abstract class CardViewHolder extends RecyclerView.ViewHolder {
        CardViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static final class HelpCardViewHolder extends CardViewHolder {
        Button dismiss;

        HelpCardViewHolder(View itemView) {
            super(itemView);
            dismiss = (Button) itemView.findViewById(R.id.dismiss);
        }
    }

    private static final class SmallCardViewHolder extends CardViewHolder {
        TextView center;

        SmallCardViewHolder(View itemView) {
            super(itemView);
            center = (TextView) itemView.findViewById(R.id.center);
        }
    }

    private static final class BigCardViewHolder extends CardViewHolder {
        TextView bottomLeft;
        TextView center;
        TextView topRight;

        BigCardViewHolder(View itemView) {
            super(itemView);
            bottomLeft = (TextView) itemView.findViewById(R.id.bottomLeft);
            center = (TextView) itemView.findViewById(R.id.center);
            topRight = (TextView) itemView.findViewById(R.id.topRight);
        }
    }

    private static final class BigBlackCardViewHolder extends CardViewHolder {
        BigBlackCardViewHolder(View itemView) {
            super(itemView);
        }
    }
}

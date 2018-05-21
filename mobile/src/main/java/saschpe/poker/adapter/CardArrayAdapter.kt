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

package saschpe.poker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.IntDef
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import saschpe.android.utils.adapter.base.ArrayAdapter
import saschpe.poker.R

class CardArrayAdapter(context: Context,
                       objects: List<String>,
                       @param:ViewType @field:ViewType private var viewType: Int,
                       private val helpViewPosition: Int) : ArrayAdapter<String, RecyclerView.ViewHolder>(objects) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var helpDismissed: Boolean = prefs.getBoolean(PREF_HELP_DISMISSED, false)
    private var onSmallCardClickListener: ((Int) -> Any)? = null

    @IntDef(BIG_CARD_VIEW_TYPE, BIG_BLACK_CARD_VIEW_TYPE, SMALL_CARD_VIEW_TYPE)
    internal annotation class ViewType

    override fun getItemViewType(position: Int): Int {
        return if (isHelpOnPosition(position)) {
            HELP_CARD_VIEW_TYPE
        } else {
            viewType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                HELP_CARD_VIEW_TYPE -> HelpCardViewHolder(inflater.inflate(R.layout.view_help_card, parent, false))
                SMALL_CARD_VIEW_TYPE -> SmallCardViewHolder(inflater.inflate(R.layout.view_small_card, parent, false))
                BIG_CARD_VIEW_TYPE -> BigCardViewHolder(inflater.inflate(R.layout.view_big_card, parent, false))
                BIG_BLACK_CARD_VIEW_TYPE -> BigBlackCardViewHolder(inflater.inflate(R.layout.view_big_black_card, parent, false))
                else -> BigCardViewHolder(inflater.inflate(R.layout.view_big_card, parent, false))
            }

    @SuppressLint("SwitchIntDef")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myPosition = position
        if (isHelpOnPosition(myPosition)) {
            val helpCardViewHolder = holder as HelpCardViewHolder
            helpCardViewHolder.dismiss.setOnClickListener {
                helpDismissed = true
                notifyItemRemoved(helpViewPosition)
                prefs.edit().putBoolean(PREF_HELP_DISMISSED, true).apply()
            }
        } else {
            if (!helpDismissed && myPosition > helpViewPosition) {
                myPosition -= 1  // Account for visible help card.
            }

            when (viewType) {
                SMALL_CARD_VIEW_TYPE -> {
                    val smallCardViewHolder = holder as SmallCardViewHolder
                    smallCardViewHolder.center.text = getItem(myPosition)
                    smallCardViewHolder.itemView.setOnClickListener { _ ->
                        onSmallCardClickListener?.invoke(holder.getAdapterPosition())
                    }
                }
                BIG_CARD_VIEW_TYPE -> {
                    val bigCardViewHolder = holder as BigCardViewHolder
                    bigCardViewHolder.bottomLeft.text = getItem(myPosition)
                    bigCardViewHolder.center.text = getItem(myPosition)
                    bigCardViewHolder.topRight.text = getItem(myPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        var count = super.getItemCount()
        if (!helpDismissed) {
            count += 1 // Account for visible help card
        }
        return count
    }

    fun setViewType(viewType: Int) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    fun setOnSmallCardClickListener(onSmallCardClickListener: (Int) -> Unit) {
        this.onSmallCardClickListener = onSmallCardClickListener
    }

    fun getSpanSizeLookup(gridLayoutManager: GridLayoutManager): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (isHelpOnPosition(position)) {
                    gridLayoutManager.spanCount
                } else {
                    1
                }
            }
        }
    }

    private fun isHelpOnPosition(position: Int): Boolean =
            !helpDismissed && position == helpViewPosition

    private class HelpCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dismiss: Button = itemView.findViewById(R.id.dismiss)
    }

    private class SmallCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val center: TextView = itemView.findViewById(R.id.center)
    }

    private class BigCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val bottomLeft: TextView = itemView.findViewById(R.id.bottomLeft)
        internal val center: TextView = itemView.findViewById(R.id.center)
        internal val topRight: TextView = itemView.findViewById(R.id.topRight)
    }

    private class BigBlackCardViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        const val BIG_CARD_VIEW_TYPE  = 1
        const val BIG_BLACK_CARD_VIEW_TYPE = 2
        const val SMALL_CARD_VIEW_TYPE = 3
        private const val HELP_CARD_VIEW_TYPE = 4
        private const val PREF_HELP_DISMISSED = "help_dismissed"
    }
}

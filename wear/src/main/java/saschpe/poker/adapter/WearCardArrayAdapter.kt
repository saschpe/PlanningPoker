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

import android.content.Context
import android.support.annotation.IntDef
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import saschpe.android.utils.adapter.base.ArrayAdapter
import saschpe.poker.R

internal class WearCardArrayAdapter(context: Context,
                                    objects: List<String>,
                                    @param:ViewType @field:ViewType private var viewType: Int) : ArrayAdapter<String, WearCardArrayAdapter.WearCardViewHolder>(objects) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @IntDef(LIGHT_CARD_VIEW_TYPE, DARK_CARD_VIEW_TYPE)
    internal annotation class ViewType

    fun setViewType(viewType: Int) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WearCardViewHolder =
            when (viewType) {
                LIGHT_CARD_VIEW_TYPE -> LightCardViewHolder(inflater.inflate(R.layout.view_light_card, parent, false))
                DARK_CARD_VIEW_TYPE -> DarkCardViewHolder(inflater.inflate(R.layout.view_dark_card, parent, false))
                else -> LightCardViewHolder(inflater.inflate(R.layout.view_light_card, parent, false))
            }

    override fun onBindViewHolder(holder: WearCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal abstract class WearCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val center: TextView = itemView.findViewById(R.id.center)

        fun bind(item: String) {
            center.text = item
        }
    }

    internal class LightCardViewHolder(itemView: View) : WearCardViewHolder(itemView)

    internal class DarkCardViewHolder(itemView: View) : WearCardViewHolder(itemView)

    companion object {
        const val LIGHT_CARD_VIEW_TYPE = 1
        const val DARK_CARD_VIEW_TYPE = 2
    }
}

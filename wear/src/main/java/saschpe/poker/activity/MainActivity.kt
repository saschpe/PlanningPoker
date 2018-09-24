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

package saschpe.poker.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.wearable.activity.WearableActivity
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import saschpe.android.utils.widget.SpacesItemDecoration
import saschpe.poker.R
import saschpe.poker.adapter.WearCardArrayAdapter
import saschpe.poker.util.PlanningPoker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : WearableActivity(), MenuItem.OnMenuItemClickListener {
    @PlanningPoker.Flavor private var flavor: Int = 0
    private var arrayAdapter: WearCardArrayAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()

        flavor = savedInstanceState?.getInt(STATE_FLAVOR, PlanningPoker.FIBONACCI) ?: PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(PREFS_FLAVOR, PlanningPoker.FIBONACCI)

        // Compute spacing between cards
        val marginDp = resources.getDimension(R.dimen.activity_horizontal_margin) / 8
        val spacePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginDp, resources.displayMetrics).toInt()

        // Setup recycler
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(SpacesItemDecoration(spacePx, layoutManager.orientation))
        arrayAdapter = WearCardArrayAdapter(this, PlanningPoker.values.get(flavor)!!, WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE)
        recycler_view.adapter = arrayAdapter
        recycler_view.scrollToPosition(PlanningPoker.defaults.get(flavor))
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view)

        // Bottom Action Drawer
        val menu = bottom_action_drawer.menu
        menuInflater.inflate(R.menu.action_drawer, menu)
        when (flavor) {
            PlanningPoker.FIBONACCI -> menu.findItem(R.id.fibonacci).isChecked = true
            PlanningPoker.T_SHIRT_SIZES -> menu.findItem(R.id.t_shirt_sizes).isChecked = true
            PlanningPoker.IDEAL_DAYS -> menu.findItem(R.id.ideal_days).isChecked = true
        }
        bottom_action_drawer.controller.peekDrawer()
        bottom_action_drawer.setOnMenuItemClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Persist current flavor for next invocation
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt(PREFS_FLAVOR, flavor)
                .apply()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current flavor over configuration change
        outState.putInt(STATE_FLAVOR, flavor)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fibonacci -> {
                updateFlavor(PlanningPoker.FIBONACCI)
                item.isChecked = true
            }
            R.id.t_shirt_sizes -> {
                updateFlavor(PlanningPoker.T_SHIRT_SIZES)
                item.isChecked = true
            }
            R.id.ideal_days -> {
                updateFlavor(PlanningPoker.IDEAL_DAYS)
                item.isChecked = true
            }
            R.id.version_info -> startActivity(Intent(this, InfoActivity::class.java))
        }
        bottom_action_drawer.controller.closeDrawer()
        return super.onOptionsItemSelected(item)
    }

    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
        updateDisplay()
    }

    override fun onUpdateAmbient() {
        super.onUpdateAmbient()
        updateDisplay()
    }

    override fun onExitAmbient() {
        updateDisplay()
        super.onExitAmbient()
    }

    private fun updateFlavor(@PlanningPoker.Flavor flavor: Int) {
        this.flavor = flavor
        arrayAdapter?.replaceAll(PlanningPoker.values.get(flavor))
        recycler_view.scrollToPosition(PlanningPoker.defaults.get(flavor))
    }

    private fun updateDisplay() {
        if (isAmbient) {
            arrayAdapter?.setViewType(WearCardArrayAdapter.DARK_CARD_VIEW_TYPE)
            clock.text = AMBIENT_DATE_FORMAT.format(Date())
            clock.visibility = View.VISIBLE
            bottom_action_drawer.controller.closeDrawer()
        } else {
            arrayAdapter?.setViewType(WearCardArrayAdapter.LIGHT_CARD_VIEW_TYPE)
            clock.visibility = View.GONE
        }
    }

    companion object {
        private const val PREFS_FLAVOR = "flavor2"
        private const val STATE_FLAVOR = "flavor"
        private val AMBIENT_DATE_FORMAT = SimpleDateFormat("HH:mm", Locale.US)
    }
}

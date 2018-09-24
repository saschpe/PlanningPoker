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

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import saschpe.android.utils.widget.SpacesItemDecoration
import saschpe.poker.R
import saschpe.poker.adapter.CardArrayAdapter
import saschpe.poker.adapter.RecyclerViewDisabler
import saschpe.poker.util.PlanningPoker
import saschpe.poker.util.ShakeDetector

class MainActivity : AppCompatActivity() {
    private var adapter: CardArrayAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var linearSnapHelper: LinearSnapHelper? = null
    @PlanningPoker.Flavor private var flavor: Int = 0
    private var recyclerViewDisabler = RecyclerViewDisabler()
    private lateinit var gridSpacesDecoration: SpacesItemDecoration
    private var isLocked: Boolean = false
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Compute spacing between cards
        val margin8dpInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()

        var linearLayoutManagerState: Parcelable? = null
        if (savedInstanceState != null) {
            flavor = savedInstanceState.getInt(STATE_FLAVOR, PlanningPoker.FIBONACCI)
            linearLayoutManagerState = savedInstanceState.getParcelable(STATE_LINEAR_LAYOUT_MANAGER)
        } else {
            // Either load flavor from previous invocation or use default
            flavor = PreferenceManager.getDefaultSharedPreferences(this)
                    .getInt(PREFS_FLAVOR, PlanningPoker.FIBONACCI)
        }

        // Recycler adapter
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val smallCardClickFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        adapter = CardArrayAdapter(this, PlanningPoker.values.get(flavor)!!, CardArrayAdapter.BIG_CARD_VIEW_TYPE, PlanningPoker.defaults.get(flavor))
        adapter?.setOnSmallCardClickListener { position ->
            smallCardClickFadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    displayBigCards()
                    recycler_view.scrollToPosition(position)
                    recycler_view.startAnimation(fadeInAnimation)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            recycler_view.startAnimation(smallCardClickFadeOutAnimation)
        }

        // Recycler layout managers
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager?.spanSizeLookup = adapter?.getSpanSizeLookup(gridLayoutManager!!)
        gridSpacesDecoration = SpacesItemDecoration(margin8dpInPx, SpacesItemDecoration.VERTICAL)

        // Recycler view
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.addItemDecoration(SpacesItemDecoration(margin8dpInPx, SpacesItemDecoration.HORIZONTAL))
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
        if (linearLayoutManagerState != null) {
            linearLayoutManager?.onRestoreInstanceState(linearLayoutManagerState)
        } else {
            recycler_view.scrollToPosition(PlanningPoker.defaults.get(flavor))
        }
        linearSnapHelper = LinearSnapHelper()
        linearSnapHelper?.attachToRecyclerView(recycler_view)

        // Card selector floating action button
        val fabClickFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fabClickFadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                if (recycler_view!!.layoutManager === linearLayoutManager) {
                    displaySmallCards()
                } else {
                    displayBigCards()
                    recycler_view!!.scrollToPosition(PlanningPoker.defaults.get(flavor))
                }
                recycler_view!!.startAnimation(fadeInAnimation)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        select_fab.setOnClickListener { recycler_view!!.startAnimation(fabClickFadeOutAnimation) }

        // Lock current card floating action button
        isLocked = false
        val lockAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        lockAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                lock()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        val unlockAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        unlockAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                unlock()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        lock_fab.setOnClickListener {
            if (isLocked) {
                recycler_view.startAnimation(unlockAnimation)
            } else {
                recycler_view.startAnimation(lockAnimation)
            }
        }

        // Shaking!
        val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
        shakeAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                recycler_view.startAnimation(unlockAnimation)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager != null) {
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            shakeDetector = ShakeDetector {
                // Only start animation in big card view mode
                if (recycler_view.layoutManager === linearLayoutManager) {
                    recycler_view.startAnimation(shakeAnimation)
                } else {
                    displayBigCards()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(shakeDetector)
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
        outState.putParcelable(STATE_LINEAR_LAYOUT_MANAGER, linearLayoutManager?.onSaveInstanceState())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        when (flavor) {
            PlanningPoker.FIBONACCI -> menu.findItem(R.id.fibonacci).isChecked = true
            PlanningPoker.T_SHIRT_SIZES -> menu.findItem(R.id.t_shirt_sizes).isChecked = true
            PlanningPoker.IDEAL_DAYS -> menu.findItem(R.id.ideal_days).isChecked = true
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.flavor).isVisible = !isLocked
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            R.id.help_and_feedback -> {
                startActivity(Intent(this, HelpActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayBigCards() {
        adapter?.setViewType(CardArrayAdapter.BIG_CARD_VIEW_TYPE)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.removeItemDecoration(gridSpacesDecoration)
        linearSnapHelper?.attachToRecyclerView(recycler_view)
        select_fab.setImageResource(R.drawable.ic_view_module)
        lock_fab.show()
    }

    private fun displaySmallCards() {
        adapter?.setViewType(CardArrayAdapter.SMALL_CARD_VIEW_TYPE)
        recycler_view.layoutManager = gridLayoutManager
        recycler_view.addItemDecoration(gridSpacesDecoration)
        linearSnapHelper?.attachToRecyclerView(null)
        select_fab.setImageResource(R.drawable.ic_view_array)
        lock_fab.hide()
    }

    private fun updateFlavor(@PlanningPoker.Flavor flavor: Int) {
        this.flavor = flavor
        adapter?.replaceAll(PlanningPoker.values.get(flavor))
        recycler_view.scrollToPosition(PlanningPoker.defaults.get(flavor))
    }

    private fun lock() {
        isLocked = true
        recycler_view.addOnItemTouchListener(recyclerViewDisabler)
        select_fab.hide()
        invalidateOptionsMenu()
        adapter?.setViewType(CardArrayAdapter.BIG_BLACK_CARD_VIEW_TYPE)
        Snackbar.make(recycler_view, R.string.shake_to_reveal, Snackbar.LENGTH_SHORT)
                .show()
    }

    private fun unlock() {
        isLocked = false
        displayBigCards()
        select_fab.show()
        invalidateOptionsMenu()
        recycler_view.removeOnItemTouchListener(recyclerViewDisabler)
    }

    companion object {
        private const val PREFS_FLAVOR = "flavor2"
        private const val STATE_FLAVOR = "flavor"
        private const val STATE_LINEAR_LAYOUT_MANAGER = "linear_layout_manager"
    }
}

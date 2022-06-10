package com.elvinliang.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

@Deprecated("BindingAdapter")
object BindingAdapter {

    @JvmStatic
    @BindingAdapter("onNavigationItemSelected")
    fun setOnNavigationItemSelected(
        view: BottomNavigationView, listener: NavigationBarView.OnItemSelectedListener?
    ) {
        view.setOnItemSelectedListener(listener)
    }

    @BindingAdapter("selectedItemPosition")
    fun setSelectedItemPosition(
        view: BottomNavigationView, position: Int
    ) {
        view.selectedItemId = position
    }
}
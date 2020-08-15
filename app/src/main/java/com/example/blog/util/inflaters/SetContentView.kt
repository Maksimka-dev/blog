package com.example.blog.util.inflaters

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.KProperty

class SetContentView<out T : ViewDataBinding>(@LayoutRes private val layoutRes: Int) {

    private var value: T? = null

    operator fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) {
            value = DataBindingUtil.setContentView(thisRef, layoutRes)
            value!!.lifecycleOwner = thisRef
        }

        return value!!
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            value = DataBindingUtil.inflate(thisRef.layoutInflater, layoutRes, null, false)
            value!!.lifecycleOwner = thisRef
        }

        return value!!
    }
}

fun <T : ViewDataBinding> contentView(@LayoutRes layoutRes: Int) =
    SetContentView<T>(layoutRes)

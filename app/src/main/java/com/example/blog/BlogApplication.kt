package com.example.blog

import android.app.Application
import com.example.blog.di.AppComponent
import com.example.blog.di.DaggerAppComponent

open class BlogApplication : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    open fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}

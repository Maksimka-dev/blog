package com.example.blog.di

import com.example.blog.ui.blog.BlogFragment
import com.example.blog.ui.main.MainActivity
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface BlogComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BlogComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: BlogFragment)
}

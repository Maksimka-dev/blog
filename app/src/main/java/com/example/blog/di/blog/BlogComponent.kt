package com.example.blog.di.blog

import com.example.blog.di.FragmentScope
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

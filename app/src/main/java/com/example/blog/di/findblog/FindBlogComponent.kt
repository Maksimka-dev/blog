package com.example.blog.di.findblog

import com.example.blog.di.FragmentScope
import com.example.blog.ui.findblog.FindBlogFragment
import com.example.blog.ui.main.MainActivity
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface FindBlogComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FindBlogComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: FindBlogFragment)
}

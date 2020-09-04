package com.example.blog.di

import com.example.blog.ui.main.MainActivity
import com.example.blog.ui.newblog.NewBlogFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface NewBlogComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): NewBlogComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: NewBlogFragment)
}

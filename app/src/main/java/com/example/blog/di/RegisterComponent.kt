package com.example.blog.di

import com.example.blog.ui.main.MainActivity
import com.example.blog.ui.register.RegisterFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface RegisterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegisterComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: RegisterFragment)
}

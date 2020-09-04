package com.example.blog.di

import com.example.blog.ui.login.LoginFragment
import com.example.blog.ui.main.MainActivity
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: LoginFragment)
}

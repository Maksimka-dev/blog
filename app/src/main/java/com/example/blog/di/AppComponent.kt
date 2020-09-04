package com.example.blog.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun blogComponent(): BlogComponent.Factory
    fun loginComponent(): LoginComponent.Factory
    fun registerComponent(): RegisterComponent.Factory
    fun chatComponent(): ChatComponent.Factory
    fun findBlogComponent(): FindBlogComponent.Factory
    fun newBlogComponent(): NewBlogComponent.Factory
}

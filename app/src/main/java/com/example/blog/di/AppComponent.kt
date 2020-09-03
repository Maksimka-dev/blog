package com.example.blog.di

import android.content.Context
import com.example.blog.di.blog.BlogComponent
import com.example.blog.di.chat.ChatComponent
import com.example.blog.di.findblog.FindBlogComponent
import com.example.blog.di.login.LoginComponent
import com.example.blog.di.newblog.NewBlogComponent
import com.example.blog.di.register.RegisterComponent
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

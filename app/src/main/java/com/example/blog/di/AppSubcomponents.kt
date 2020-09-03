package com.example.blog.di

import com.example.blog.di.blog.BlogComponent
import com.example.blog.di.chat.ChatComponent
import com.example.blog.di.findblog.FindBlogComponent
import com.example.blog.di.login.LoginComponent
import com.example.blog.di.newblog.NewBlogComponent
import com.example.blog.di.register.RegisterComponent
import dagger.Module

@Module(
    subcomponents = [
        BlogComponent::class,
        LoginComponent::class,
        RegisterComponent::class,
        ChatComponent::class,
        FindBlogComponent::class,
        NewBlogComponent::class
    ]
)
class AppSubcomponents

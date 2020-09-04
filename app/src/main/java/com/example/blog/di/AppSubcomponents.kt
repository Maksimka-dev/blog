package com.example.blog.di

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

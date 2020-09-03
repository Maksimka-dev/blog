package com.example.blog.di.chat

import com.example.blog.di.FragmentScope
import com.example.blog.ui.chat.ChatFragment
import com.example.blog.ui.main.MainActivity
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface ChatComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ChatComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: ChatFragment)
}

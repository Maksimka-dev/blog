@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.blog.Blog
import com.example.blog.model.chat.Chat
import com.example.blog.model.chat.ChatRepository
import com.example.blog.model.chat.Message
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ChatViewModel @Inject constructor(mAuth: FirebaseAuth, val chatRepository: ChatRepository) :
    ViewModel() {
    var blog: Blog? = Blog()

    val openGalleryCommand = SingleLiveEvent<Void>()
    val displayAdminCommand = SingleLiveEvent<Void>()
    val hidePreviewCommand = SingleLiveEvent<Void>()

    var bitmapImage: Bitmap? = null
    var defaultBitmap: Bitmap? = null

    var chat: MutableLiveData<Chat> = mutableLiveData()
    val textField = mutableLiveData("")

    private val user = mAuth.currentUser

    init {
        chat = chatRepository.chat
    }

    fun loadChat() {
        if (blog != null) {
            chatRepository.blog = blog!!
            chatRepository.getMessages()
        }
    }

    fun onSendBtnClick() {
        if (blog != null && user!!.uid == blog!!.ownerId) {

            val date = Date()
            val dateFormat = SimpleDateFormat("MM.dd hh:mm", Locale.getDefault())

            if (bitmapImage == null && defaultBitmap != null) {
                bitmapImage = defaultBitmap
            }

            val message = Message(
                text = textField.value.toString(),
                time = dateFormat.format(date),
                picUrl = "",
                pic = bitmapImage
            )
            chatRepository.sendMessage(message, blog!!)

            hidePreviewCommand.call()
        } else displayNotAdmin()
    }

    fun onClipClick() {
        openGalleryCommand.call()
    }

    private fun displayNotAdmin() {
        displayAdminCommand.call()
    }
}

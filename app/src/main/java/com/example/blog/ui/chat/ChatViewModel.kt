@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.Blog
import com.example.blog.model.Chat
import com.example.blog.model.ChatRepository
import com.example.blog.model.Message
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {
    var blog: Blog? = Blog()

    val openGalleryCommand = SingleLiveEvent<Void>()
    val internetCommand = SingleLiveEvent<Void>()
    val displayInternetCommand = SingleLiveEvent<Void>()
    val displayAdminCommand = SingleLiveEvent<Void>()
    val hidePreviewCommand = SingleLiveEvent<Void>()

    var isInternetAvailable = true
    var bitmapImage: Bitmap? = null
    var defaultBitmap: Bitmap? = null

    var chat: MutableLiveData<Chat> = mutableLiveData()
    val textField = mutableLiveData("")

    private val user = FirebaseAuth.getInstance().currentUser
    private val chatRepository: ChatRepository = ChatRepository()

    init {
        chat = chatRepository.chat
    }

    fun loadChat() {
        if (isNetworkConnected()) {
            if (blog != null) {
                chatRepository.blog = blog!!
                chatRepository.getMessages()
            }
        } else displayNoConnection()
    }

    fun onSendBtnClick() {
        if (isNetworkConnected()) {
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
        } else displayNoConnection()
    }

    fun onClipClick() {
        openGalleryCommand.call()
    }

    private fun displayNotAdmin() {
        displayAdminCommand.call()
    }

    private fun displayNoConnection() {
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}

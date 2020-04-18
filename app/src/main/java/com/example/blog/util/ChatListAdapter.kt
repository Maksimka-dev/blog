package com.example.blog.util

import android.graphics.Bitmap
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R

class ChatListAdapter(
    private val messages: ArrayList<String>,
    private val pics: ArrayList<Bitmap?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
                = LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatHolder: ChatViewHolder = holder as ChatViewHolder

        val message: String = messages[position]
        val pic: Bitmap? = pics[position]

        chatHolder.bind(message, pic)
    }

    class ChatViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private val textView: TextView = view.findViewById(R.id.messageText)
        private val imageView: ImageView = view.findViewById(R.id.messagePic)

        fun bind(message: String, pic: Bitmap?){
            textView.text = message
            if (pic != null) {
                imageView.setImageBitmap(pic)
            }
        }
    }
}


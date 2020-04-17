package com.example.blog.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R

class ChatListAdapter(
    private val messages: ArrayList<String>
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

        chatHolder.bind(message)
    }

    class ChatViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.messageText)
        val imageView: ImageView = view.findViewById(R.id.messagePic)

        fun bind(message: String){
            textView.text = message
            //imageView = image
        }
    }
}


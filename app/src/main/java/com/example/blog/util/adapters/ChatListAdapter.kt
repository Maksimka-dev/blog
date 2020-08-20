package com.example.blog.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blog.databinding.MessageBinding
import com.example.blog.ui.chat.ChatViewModel
import com.example.blog.util.view.AdapterAvatarData
import com.example.blog.util.view.AdapterMessageData
import com.example.blog.util.view.AdapterTimeData

class ChatListAdapter(
    private val model: ChatViewModel,
    private var data: Triple<AdapterMessageData, AdapterAvatarData, AdapterTimeData> = Triple(
        emptyList(), emptyList(), emptyList()
    )
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.first.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = data.first[position]
        val picUrl = data.second[position]
        val time = data.third[position]

        holder.binding.model = model
        with(holder.binding) {
            messageText.text = message
            timeTV.text = time

            Glide.with(messagePic)
                .load(picUrl)
                .into(messagePic)
        }
    }

    fun setData(
        blogData: AdapterMessageData,
        avatarData: AdapterAvatarData,
        timeData: AdapterTimeData
    ) {
        data = Triple(blogData, avatarData, timeData)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root)
}

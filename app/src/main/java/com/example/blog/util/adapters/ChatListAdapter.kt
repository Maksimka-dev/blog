@file:Suppress("DEPRECATION")

package com.example.blog.util.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.blog.R
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
                .asBitmap()
                .load(picUrl)
                .transition(withCrossFade())
                .apply(
                    RequestOptions().transform(RoundedCorners(15)).error(R.drawable.message_round)
                        .skipMemoryCache(true).diskCacheStrategy(
                            DiskCacheStrategy.NONE
                        )
                )
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        messagePic.setImageBitmap(resource)
                    }
                })
        }
    }

    fun addData(
        blogData: AdapterMessageData,
        avatarData: AdapterAvatarData,
        timeData: AdapterTimeData
    ) {
        val position = data.first.size
        val count = blogData.size - data.first.size

        data = Triple(blogData, avatarData, timeData)
        notifyItemRangeInserted(position, count)
    }

    class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root)
}

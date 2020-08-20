package com.example.blog.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blog.databinding.ChannelFindblogBinding
import com.example.blog.ui.findblog.FindBlogViewModel
import com.example.blog.util.view.AdapterAvatarData
import com.example.blog.util.view.AdapterBlogData

class FindBlogListAdapter(
    private val model: FindBlogViewModel,
    private var data: Pair<AdapterBlogData, AdapterAvatarData> = Pair(emptyList(), emptyList())
) : RecyclerView.Adapter<FindBlogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChannelFindblogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = data.first[position]
        val avatarUrl = data.second[position]

        val title = blog.title
        var time = ""
        var lastMsg = ""

        if (blog.time.isNotEmpty()) {
            time = blog.time.last().toString()
        }

        if (blog.messages.isNotEmpty()) {
            lastMsg = blog.messages.last().toString()
        }

        holder.binding.model = model
        with(holder.binding) {
            this.blog = blog
            this.channelLastMessage.text = lastMsg
            this.time.text = time
            this.title.text = title

            Glide.with(channelAvatar)
                .load(avatarUrl)
                .into(channelAvatar)
        }
    }

    override fun getItemCount() = data.first.size

    fun setData(blogData: AdapterBlogData, avatarData: AdapterAvatarData) {
        data = Pair(blogData, avatarData)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ChannelFindblogBinding) : RecyclerView.ViewHolder(binding.root)
}

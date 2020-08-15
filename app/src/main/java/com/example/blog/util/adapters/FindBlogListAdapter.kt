package com.example.blog.util.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.databinding.ChannelFindblogBinding
import com.example.blog.model.Blog
import com.example.blog.ui.findblog.FindBlogViewModel

typealias AdapterBlogData = List<Blog>

class FindBlogListAdapter(
    private val model: FindBlogViewModel,
    private var data: Pair<AdapterBlogData, AdapterBitmapData> = Pair(emptyList(), emptyList())
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
        val avatar = data.second[position]

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
        holder.binding.blog = blog

        holder.binding.channelLastMessage.text = lastMsg
        holder.binding.time.text = time
        holder.binding.title.text = title

        if (avatar != null) holder.binding.channelAvatar.setImageBitmap(avatar)
    }

    override fun getItemCount() = data.first.size

    fun setData(blogData: AdapterBlogData, bitmapData: AdapterBitmapData) {
        data = Pair(blogData, bitmapData)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ChannelFindblogBinding) : RecyclerView.ViewHolder(binding.root)
}

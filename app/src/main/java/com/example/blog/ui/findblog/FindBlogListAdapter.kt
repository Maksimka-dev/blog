package com.example.blog.ui.findblog

import com.example.blog.ui.blog.Blog
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.databinding.ChannelFindblogBinding

typealias AdapterBlogData = List<Blog>
typealias AdapterBitmapData = List<Bitmap?>

class FindBlogListAdapter   (
    private val model: FindBlogViewModel,
    private var data: Pair<AdapterBlogData, AdapterBitmapData> = Pair(emptyList(), emptyList())
) : RecyclerView.Adapter<FindBlogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChannelFindblogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = data.first[position]
        val avatar = data.second[position]

        val title = blog.title
        var time: String = ""
        var lastMsg: String = ""

        if (blog.time.isNotEmpty()) {
            time = blog.time.last().toString()
        }

        if (blog.messages.isNotEmpty()){
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
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                data.first,
                blogData
            )
        )
        data = Pair(blogData, bitmapData)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: ChannelFindblogBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback(
        private val oldBlogData: AdapterBlogData,
        private val newBlogData: AdapterBlogData
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldBlogData.size

        override fun getNewListSize() = newBlogData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldBlogData[oldItemPosition] == newBlogData[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldBlogData[oldItemPosition] == newBlogData[newItemPosition]
    }
}
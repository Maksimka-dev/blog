package com.example.blog.ui.chat

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.databinding.MessageBinding

typealias AdapterMessageData = List<String>
typealias AdapterBitmapData = List<Bitmap?>
typealias AdapterTimeData = List<String>

class ChatListAdapter(
    private val model: ChatViewModel,
    private var data: Triple<AdapterMessageData, AdapterBitmapData, AdapterTimeData> = Triple(
        emptyList(), emptyList(), emptyList())
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.first.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = data.first[position]
        val bitmap = data.second[position]
        val time = data.third[position]

        holder.binding.model = model

        holder.binding.messageText.text = message
        holder.binding.timeTV.text = time
        holder.binding.messagePic.setImageBitmap(bitmap)
    }

    fun setData(blogData: AdapterMessageData, bitmapData: AdapterBitmapData, timeData: AdapterTimeData) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                data.first,
                blogData
            )
        )
        data = Triple(blogData, bitmapData, timeData)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: MessageBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback(
        private val oldMessageData: AdapterMessageData,
        private val newMessageData: AdapterMessageData
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldMessageData.size

        override fun getNewListSize() = newMessageData.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessageData[oldItemPosition] == newMessageData[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMessageData[oldItemPosition] == newMessageData[newItemPosition]
    }
}


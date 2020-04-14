package com.example.blog.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog


class BlogListAdapter(
    private val inflater: LayoutInflater,
    private val blogArrayList: ArrayList<Blog>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogViewHolder(inflater.inflate(R.layout.channel, parent, false))
    }

    override fun getItemCount(): Int {
        return blogArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val blogHolder: BlogViewHolder = holder as BlogViewHolder

        blogHolder.avatar.setImageResource(blogArrayList[position].avatar)
        blogHolder.title.text = blogArrayList[position].title
        blogHolder.lastMsg.text = blogArrayList[position].lastMsg
        blogHolder.unreadMsg.text = blogArrayList[position].unreadMsg
        blogHolder.time.text = blogArrayList[position].time
    }

    class BlogViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.channelAvatar)
        val title: TextView = view.findViewById(R.id.title)
        val lastMsg: TextView = view.findViewById(R.id.channelLastMessage)
        val time: TextView = view.findViewById(R.id.time)
        val unreadMsg: TextView = view.findViewById(R.id.unreadMes)
    }
}


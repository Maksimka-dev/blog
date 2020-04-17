package com.example.blog.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog


class BlogListAdapter(
    private val itemClickListener: OnItemClickListener,
    private val blogArrayList: ArrayList<Blog>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.channel, parent, false)
        return BlogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return blogArrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val blogHolder: BlogViewHolder = holder as BlogViewHolder

        val blog = blogArrayList[position]

        blogHolder.bind(blog, itemClickListener)
    }

    class BlogViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        //val avatar: ImageView = view.findViewById(R.id.channelAvatar)
        val title: TextView = view.findViewById(R.id.title)
        val lastMsg: TextView = view.findViewById(R.id.channelLastMessage)
        val time: TextView = view.findViewById(R.id.time)
        val unreadMsg: TextView = view.findViewById(R.id.unreadMes)

        fun bind(blog: Blog, clickListener: OnItemClickListener){
            title.text = blog.title
            lastMsg.text = blog.lastMsg
            time.text = blog.time
            unreadMsg.text = blog.unreadMsg

            itemView.setOnClickListener {
                clickListener.onItemClicked(blog)
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(blog: Blog)
}


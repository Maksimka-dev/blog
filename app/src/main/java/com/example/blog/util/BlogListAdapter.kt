package com.example.blog.util

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog


class BlogListAdapter(
    private val itemClickListener: OnItemClickListener,
    private val blogArrayList: ArrayList<Blog>,
    private val avatarList: ArrayList<Bitmap?>
)
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
        val avatar = avatarList[position]

        blogHolder.bind(blog, avatar, itemClickListener)
    }

    class BlogViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val avatarIV: de.hdodenhof.circleimageview.CircleImageView = view.findViewById(R.id.channelAvatar)
        private val title: TextView = view.findViewById(R.id.title)
        private val lastMsg: TextView = view.findViewById(R.id.channelLastMessage)
        private val time: TextView = view.findViewById(R.id.time)
        private val unreadMsg: TextView = view.findViewById(R.id.unreadMes)

        fun bind(blog: Blog, avatar: Bitmap?, clickListener: OnItemClickListener){
            title.text = blog.title
            lastMsg.text = blog.lastMsg
            time.text = blog.time
            unreadMsg.text = blog.unreadMsg

            if (avatar != null){
                avatarIV.setImageBitmap(avatar)
            }

            itemView.setOnClickListener {
                clickListener.onItemClicked(blog)
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(blog: Blog)
}


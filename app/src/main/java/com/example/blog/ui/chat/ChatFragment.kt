package com.example.blog.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog
import com.example.blog.databinding.ChatFragmentBinding
import com.example.blog.util.ChatListAdapter
import com.example.blog.util.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton




class ChatFragment : Fragment(){

    private lateinit var viewModel: ChatViewModel

    companion object{
        fun newInstance()
           = ChatFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ChatFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel = binding.viewModel as ChatViewModel

        viewModel.activity = activity!!
        viewModel.context = context!!
        viewModel.bundle = this.arguments

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.createView()

        val recyclerView: RecyclerView = activity!!.findViewById(R.id.messagesRecyclerView)

        val liveData: MutableLiveData<Boolean> = viewModel.chatLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true) {

                val refreshedAdapter = ChatListAdapter(viewModel.messagesArrayList)
                recyclerView.adapter = refreshedAdapter

                liveData.value = false
            }
        })

        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = ""

        val refreshFab: FloatingActionButton = activity!!.findViewById(R.id.refreshBtn)
        refreshFab.hide()

        val addFab: FloatingActionButton = activity!!.findViewById(R.id.addFab)
        addFab.hide()
    }
}
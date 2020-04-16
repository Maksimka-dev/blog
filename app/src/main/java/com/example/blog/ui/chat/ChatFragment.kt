package com.example.blog.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.blog.R
import com.example.blog.databinding.ChatFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatFragment : Fragment() {

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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = ""

        val refreshFab: FloatingActionButton = activity!!.findViewById(R.id.refreshBtn)
        refreshFab.hide()

        val addFab: FloatingActionButton = activity!!.findViewById(R.id.addFab)
        addFab.hide()
    }
}
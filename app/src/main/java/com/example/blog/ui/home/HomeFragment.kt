package com.example.blog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog
import com.example.blog.blog.BlogFragment
import com.example.blog.databinding.FragmentHomeBinding
import com.example.blog.ui.chat.ChatFragment
import com.example.blog.ui.signIn.SignInFragment
import com.example.blog.util.BlogListAdapter
import com.example.blog.util.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel

    companion object{
        fun newInstance()
            = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel = binding.viewModel as HomeViewModel
        homeViewModel.activity = activity!!
        homeViewModel.context = context!!

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel.init()
        val recyclerView: RecyclerView = activity!!.findViewById(R.id.recyclerView)

        val liveData: MutableLiveData<Boolean> = homeViewModel.blogLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true) {
                val refreshedAdapter = BlogListAdapter(this, homeViewModel.blogArrayList, homeViewModel.avatarList)
                recyclerView.adapter = refreshedAdapter

                liveData.value = false
            }
        })


        val userLiveData = homeViewModel.userLiveData
        userLiveData.observe(viewLifecycleOwner, Observer {
            if (userLiveData.value != null) {

            }
        })
    }

    override fun onItemClicked(blog: Blog) {
        val bundle = Bundle()
        bundle.putString("title", blog.title)
        bundle.putString("blogId", blog.blogId)
        bundle.putString("ownerId", blog.ownerId)
        bundle.putStringArrayList("time", blog.time)
        bundle.putStringArrayList("messages", blog.messages)

        val fragment = ChatFragment.newInstance()
        fragment.arguments = bundle


    }
}
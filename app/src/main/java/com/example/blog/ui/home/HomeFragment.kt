package com.example.blog.ui.home

import android.os.Bundle
import android.util.Log
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
import com.example.blog.blog.BlogFragment
import com.example.blog.databinding.FragmentHomeBinding
import com.example.blog.ui.signIn.SignInFragment
import com.example.blog.util.BlogListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAuth: FirebaseAuth

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
        homeViewModel.init()

        activity!!.supportFragmentManager.beginTransaction().remove(SignInFragment.newInstance()).commit()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //val blog1 = Blog()
        //blog1.avatar = R.mipmap.euro
        //blog1.title = "1xBet"
        //blog1.lastMsg = "Mousesports vs Virtus.pro 1.56x1.01"
        //blog1.unreadMsg = "5"
        //blog1.time = "11:47"

        //val blog2 = Blog()
        //blog2.avatar = R.mipmap.virus
        //blog2.title = "Coronavirus"
        //blog2.lastMsg = "Take care of yourself!"
        //blog2.unreadMsg = "3"
        //blog2.time = "10:53"

        val recyclerView: RecyclerView = activity!!.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = BlogListAdapter(layoutInflater, homeViewModel.blogArrayList)
        recyclerView.adapter = adapter

        val liveData: MutableLiveData<Boolean> = homeViewModel.blogLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true) {
                val refreshedAdapter = BlogListAdapter(layoutInflater, homeViewModel.blogArrayList)
                recyclerView.adapter = refreshedAdapter

                liveData.value = false
            }
        })


        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = "Blogs"

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()

        val refreshFab: FloatingActionButton = activity!!.findViewById(R.id.refreshBtn)
        refreshFab.show()

        val addFab: FloatingActionButton = activity!!.findViewById(R.id.addFab)
        addFab.show()

        val userLiveData = homeViewModel.userLiveData
        userLiveData.observe(viewLifecycleOwner, Observer {
            if (userLiveData.value != null) {
                addFab.setOnClickListener{
                    activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, BlogFragment.newInstance())
                        .addToBackStack("blog")
                        .commit()
                }
            }
        })
    }
}
package com.example.blog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.blog.Blog
import com.example.blog.blog.BlogFragment
import com.example.blog.databinding.FragmentHomeBinding
import com.example.blog.ui.signIn.SignInFragment
import com.example.blog.util.BlogListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

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

        activity!!.supportFragmentManager.beginTransaction().remove(SignInFragment.newInstance()).commit()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val blog1 = Blog()
        blog1.avatar = R.drawable.euro
        blog1.title = "1xBet"
        blog1.lastMsg = "Mousesports vs Virtus.pro 1.56x1.01"
        blog1.unreadMsg = "5"
        blog1.time = "11:47"

        val blog2 = Blog()
        blog2.avatar = R.drawable.virus
        blog2.title = "Coronavirus"
        blog2.lastMsg = "Take care of yourself!"
        blog2.unreadMsg = "3"
        blog2.time = "10:53"

        val recyclerView = activity!!.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = BlogListAdapter(layoutInflater, arrayOf(blog1, blog2))
        recyclerView.adapter = adapter

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user != null) {
            val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
            fab.show()

            fab.setOnClickListener {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, BlogFragment.newInstance())
                    .addToBackStack("blog")
                    .commit()
            }
        }
    }
}
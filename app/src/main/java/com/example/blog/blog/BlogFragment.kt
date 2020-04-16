package com.example.blog.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blog.ui.chat.ChatFragment
import com.example.blog.R
import com.example.blog.databinding.BlogFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class BlogFragment : Fragment() {

    private lateinit var blogViewModel: BlogViewModel

    companion object{
        fun newInstance()
            = BlogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BlogFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.blog_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(BlogViewModel::class.java)
        blogViewModel = binding.viewModel as BlogViewModel

        blogViewModel.activity = activity!!
        blogViewModel.context = context!!

        val user = FirebaseAuth.getInstance().currentUser
        blogViewModel.userId = user!!.uid


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = "Create blog"

        val fab = activity!!.findViewById<FloatingActionButton>(R.id.fab)
        fab.hide()

        val confirmFab = activity!!.findViewById<FloatingActionButton>(R.id.confirmFab)
        confirmFab.show()

        val liveData = blogViewModel.changeFragmentLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true){
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, ChatFragment.newInstance())
                    .addToBackStack("blog")
                    .commit()

                liveData.value = false
            }
        })
    }
}
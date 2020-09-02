package com.example.blog.ui.findblog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentFindBlogBinding
import com.example.blog.util.adapters.FindBlogListAdapter
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.viewmodel.viewModel

class FindBlogFragment : Fragment() {
    private val binding by contentView<FragmentFindBlogBinding>(R.layout.fragment_find_blog)
    private val model by viewModel<FindBlogViewModel>()
    private lateinit var adapter: FindBlogListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.model = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        adapter = FindBlogListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.loadUser()

        model.user.observe(viewLifecycleOwner, Observer {
            model.isUserReady = true
        })

        model.searchCommand.observe(this) {
            model.loadBlogs()
        }

        model.blogs.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                model.loadBlogsPicsUrls()
            }
        })

        model.blogsAvatars.observe(viewLifecycleOwner, Observer {
            model.prepareData()
        })

        model.blogsData.observe(this) {
            if (it != null) {
                adapter.setData(blogData = it.first, avatarData = it.second)
            }
        }

        model.subscription.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navController.navigate(R.id.action_findBlogFragment_to_blogFragment)
            }
        })
    }
}

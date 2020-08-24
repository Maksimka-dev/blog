package com.example.blog.ui.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentBlogBinding
import com.example.blog.util.adapters.BlogListAdapter
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.viewmodel.viewModel

class BlogFragment : Fragment() {
    private val binding by contentView<FragmentBlogBinding>(R.layout.fragment_blog)
    private val model by viewModel<BlogViewModel>()
    private lateinit var adapter: BlogListAdapter
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

        adapter = BlogListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.loadUser()

        model.user.observe(viewLifecycleOwner, Observer {
            model.loadBlogs()
        })

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

        model.openCommand.observe(this) {
            val action = BlogFragmentDirections.actionBlogFragmentToChatFragment(model.blog)
            navController.navigate(action)
        }

        model.createBlogCommand.observe(this) {
            navController.navigate(R.id.action_blogFragment_to_newBlogFragment)
        }

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(requireActivity())
        }

        model.displayInternetCommand.observe(this) {
            Toast.makeText(context, NO_INTERNET, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
}

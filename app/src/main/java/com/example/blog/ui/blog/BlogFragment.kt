package com.example.blog.ui.blog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentBlogBinding
import com.example.blog.model.user.User
import com.example.blog.ui.main.MainActivity
import com.example.blog.util.adapters.BlogListAdapter
import com.example.blog.util.inflaters.contentView
import javax.inject.Inject

class BlogFragment : Fragment() {
    private val binding by contentView<FragmentBlogBinding>(R.layout.fragment_blog)
    private lateinit var adapter: BlogListAdapter
    private lateinit var navController: NavController

    @Inject
    lateinit var model: BlogViewModel

    @Inject
    lateinit var user: MutableLiveData<User?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.model = model
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).blogComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        adapter = BlogListAdapter(model)
        binding.recyclerView.adapter = adapter

        user.observe(viewLifecycleOwner, {
            it?.let {
                model.user.value = it
                model.loadBlogs()
            }
        })

        model.blogs.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                model.loadBlogsPicsUrls()
            }
        })

        model.blogsAvatars.observe(viewLifecycleOwner, {
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
    }
}

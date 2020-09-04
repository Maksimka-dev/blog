package com.example.blog.ui.findblog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentFindBlogBinding
import com.example.blog.model.user.User
import com.example.blog.ui.main.MainActivity
import com.example.blog.util.adapters.FindBlogListAdapter
import com.example.blog.util.inflaters.contentView
import javax.inject.Inject

class FindBlogFragment : Fragment() {
    private val binding by contentView<FragmentFindBlogBinding>(R.layout.fragment_find_blog)
    private lateinit var adapter: FindBlogListAdapter
    private lateinit var navController: NavController

    @Inject
    lateinit var user: MutableLiveData<User?>

    @Inject
    lateinit var model: FindBlogViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).findBlogComponent.inject(this)
    }

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

        user.observe(viewLifecycleOwner, {
            it?.let {
                model.user.value = it
                model.isUserReady = true
            }
        })

        model.searchCommand.observe(this) {
            model.loadBlogs()
        }

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

        model.subscription.observe(viewLifecycleOwner, {
            if (it) {
                navController.navigate(R.id.action_findBlogFragment_to_blogFragment)
            }
        })
    }
}

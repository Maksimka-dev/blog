package com.example.blog.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.blog.R
import com.example.blog.databinding.BlogFragmentBinding

class BlogFragment : Fragment() {

    private lateinit var blogViewModel: BlogViewModel

    companion object {
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

        return binding.root
    }
}
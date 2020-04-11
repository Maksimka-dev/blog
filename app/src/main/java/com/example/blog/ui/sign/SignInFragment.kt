package com.example.blog.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.blog.R
import com.example.blog.databinding.SignInFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignInFragment : Fragment() {
    companion object {
        fun newInstance()
            = SignInFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SignInFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()
    }
}
package com.example.blog.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.blog.databinding.SignUpFragmentBinding
import com.example.blog.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignUpFragment : Fragment() {
    companion object{
        fun newInstance()
            = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SignUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()

    }
}
package com.example.blog.ui.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blog.R
import com.example.blog.databinding.SignInFragmentBinding
import com.example.blog.ui.home.HomeFragment
import com.example.blog.ui.signUp.SignUpFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var viewModel: SignInViewModel

    companion object {
        fun newInstance()
            = SignInFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {

            val binding: SignInFragmentBinding =
                DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)
            binding.viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
            viewModel = binding.viewModel as SignInViewModel
            viewModel.activity = activity!!
            viewModel.context = context!!

            return binding.root
        } else {
            return inflater.inflate(R.layout.fragment_signed_in, container, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val signUpTW = activity!!.findViewById<TextView>(R.id.signUpTextView)
            signUpTW.setOnClickListener {

                activity!!.supportFragmentManager.popBackStack()
            }
        }

        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)

        val liveData = viewModel.liveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true){

                activity!!.supportFragmentManager.popBackStack()
            }
        })
    }
}
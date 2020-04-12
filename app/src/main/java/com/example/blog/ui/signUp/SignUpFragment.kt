package com.example.blog.ui.signUp

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
import com.example.blog.databinding.SignUpFragmentBinding
import com.example.blog.ui.home.HomeFragment
import com.example.blog.ui.signIn.SignInFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignUpFragment : Fragment() {
    private lateinit var viewModel: SignUpViewModel

    companion object{
        fun newInstance()
            = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SignUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        viewModel = (binding.viewModel as SignUpViewModel)
        viewModel.activity = activity!!
        viewModel.context = context!!
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fab: FloatingActionButton = activity!!.findViewById(R.id.fab)
        fab.hide()

        val signUpTW = activity!!.findViewById<TextView>(R.id.signInTW)
        signUpTW.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment,
                    SignInFragment.newInstance()
                )
                .commit()
            activity!!.supportFragmentManager.popBackStack()
        }

        val liveData = viewModel.liveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true){
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment.newInstance())
                    .commit()
                activity!!.supportFragmentManager.popBackStack()
            }
        })
    }
}
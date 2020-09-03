package com.example.blog.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentRegisterBinding
import com.example.blog.ui.main.MainActivity
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.view.WRONG_CREDENTIALS
import javax.inject.Inject

class RegisterFragment : Fragment() {
    private val binding by contentView<FragmentRegisterBinding>(R.layout.fragment_register)
    private lateinit var navController: NavController

    @Inject
    lateinit var model: RegisterViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).registerComponent.inject(this)
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

        binding.signIn.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        model.registerSuccessful.observe(this) {
            navController.navigate(R.id.action_registerFragment_to_blogFragment)
        }

        model.validationErrorCommand.observe(this) {
            Toast.makeText(context, WRONG_CREDENTIALS, Toast.LENGTH_SHORT).show()
        }

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(requireActivity())
        }

        model.displayInternetCommand.observe(this) {
            Toast.makeText(activity, NO_INTERNET, Toast.LENGTH_SHORT).show()
        }
    }
}

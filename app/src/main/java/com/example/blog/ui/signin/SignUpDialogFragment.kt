package com.example.blog.ui.signin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.blog.R
import com.example.blog.databinding.FragmentSignupDialogBinding
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.view.WRONG_CREDENTIALS
import com.example.blog.util.viewmodel.viewModel

class SignUpDialogFragment : DialogFragment() {
    private val model by viewModel<SignUpDialogViewModel>()
    private lateinit var listener: Listener

    companion object {
        const val GO = "Go"
        const val CANCEL = "Cancel"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = setupDialog()

        model.validationErrorCommand.observe(this) {
            Toast.makeText(context, WRONG_CREDENTIALS, Toast.LENGTH_SHORT).show()
        }

        model.loggedInCommand.observe(this) {
            model.email.value?.let {
                listener.onLogin(it)
            }
        }

        model.cancelledCommand.observe(this) {
            listener.onCancel()
        }

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(requireActivity())
        }

        model.displayInternetCommand.observe(this) {
            Toast.makeText(activity, NO_INTERNET, Toast.LENGTH_SHORT).show()
        }

        return dialog
    }

    private fun setupDialog(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_signup_dialog, null, false)

        val binding = FragmentSignupDialogBinding.bind(view)
        binding.lifecycleOwner = this
        binding.model = model

        val dialog = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setPositiveButton(GO, null)
            .setNegativeButton(CANCEL, null)
            .create()

        isCancelable = false

        // Workaround to prevent buttons to close the dialog
        // https://stackoverflow.com/q/2620444/4899346
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                model.handleLoginButtonClick()
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                model.handleCancel()
            }
        }

        return dialog
    }

    interface Listener {
        fun onLogin(email: String)
        fun onCancel()
    }
}

package com.example.blog.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.blog.R
import com.example.blog.databinding.FragmentLoginDialogBinding
import com.example.blog.viewmodel.viewModel


class LogInDialogFragment : DialogFragment() {

    private val model by viewModel<LogInDialogViewModel>()

    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = setupDialog()

        model.validationErrorCommand.observe(this) {
            Toast.makeText(context, "Wrong credentials :(", Toast.LENGTH_SHORT).show()
        }

        model.loggedInCommand.observe(this) {
            listener.onLogin(model.username.value!!)
        }

        model.cancelledCommand.observe(this) {
            listener.onCancel()
        }

        return dialog
    }

    private fun setupDialog(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_login_dialog, null, false)

        val binding = FragmentLoginDialogBinding.bind(view)
        binding.lifecycleOwner = this
        binding.model = model

        val dialog = AlertDialog.Builder(activity!!)
            .setView(view)
            .setPositiveButton("Go", null)
            .setNegativeButton("Cancel", null)
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
        fun onLogin(username: String)
        fun onCancel()
    }
}
package com.example.blog.newblog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.blog.R
import com.example.blog.databinding.FragmentNewblogDialogBinding
import com.example.blog.viewmodel.viewModel

class NewBlogDialogFragment : DialogFragment() {
    private val model by viewModel<NewBlogViewModel>()
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

        model.createCommand.observe(this) {
            listener.onCreateBlog(model.title.value!!)
        }

        model.cancelledCommand.observe(this) {
            listener.onCancel()
        }

        return dialog
    }

    private fun setupDialog(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_newblog_dialog, null, false)

        val binding = FragmentNewblogDialogBinding.bind(view)
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
                model.handleCreateButtonClick()
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                model.handleCancel()
            }
        }

        return dialog
    }

    interface Listener {
        fun onCreateBlog(title: String)
        fun onCancel()
    }
}

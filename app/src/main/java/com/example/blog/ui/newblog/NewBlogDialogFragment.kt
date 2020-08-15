@file:Suppress("DEPRECATION")

package com.example.blog.ui.newblog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.blog.R
import com.example.blog.databinding.FragmentNewblogDialogBinding
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.viewmodel.viewModel

class NewBlogDialogFragment : DialogFragment() {
    private val model by viewModel<NewBlogViewModel>()
    private lateinit var binding: FragmentNewblogDialogBinding
    private lateinit var listener: Listener

    companion object {
        const val SELECT_ICON = "Please select blog icon"
        const val TITLE_TOO_SHORT = "Title is too short"
        const val GO = "Go"
        const val CANCEL = "Cancel"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as Listener
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = setupDialog()

        model.validationErrorCommand.observe(this) {
            Toast.makeText(context, TITLE_TOO_SHORT, Toast.LENGTH_SHORT).show()
        }

        model.createCommand.observe(this) {
            model.title.value?.let {
                listener.onCreateBlog(it)
            }
        }

        model.cancelledCommand.observe(this) {
            listener.onCancel()
        }

        model.avatarCommand.observe(this) {
            Toast.makeText(context, SELECT_ICON, Toast.LENGTH_SHORT).show()
        }

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(requireActivity())
        }

        model.displayInternetCommand.observe(this) {
            Toast.makeText(activity, NO_INTERNET, Toast.LENGTH_SHORT).show()
        }

        return dialog
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setupDialog(): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_newblog_dialog, null, false)

        binding = FragmentNewblogDialogBinding.bind(view)
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
                model.handleCreateButtonClick()
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                model.handleCancel()
            }
        }

        binding.blogPic.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000)
            } else {
                startGallery()
            }
        }

        return dialog
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                val returnUri: Uri? = data?.data
                val bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)

                binding.blogPic.setImageBitmap(bitmapImage)

                model.bitmapImage = bitmapImage
            }
        }
    }

    interface Listener {
        fun onCreateBlog(title: String)
        fun onCancel()
    }
}

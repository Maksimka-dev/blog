@file:Suppress("DEPRECATION")

package com.example.blog.blog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blog.ui.chat.ChatFragment
import com.example.blog.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class BlogFragment : Fragment() {

    //private lateinit var blogViewModel: BlogViewModel
//
    //companion object{
    //    fun newInstance()
    //        = BlogFragment()
    //}
//
    //override fun onCreateView(
    //    inflater: LayoutInflater,
    //    container: ViewGroup?,
    //    savedInstanceState: Bundle?
    //): View? {
    //    val binding: BlogFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.blog_fragment, container, false)
    //    binding.viewModel = ViewModelProviders.of(this).get(BlogViewModel::class.java)
    //    blogViewModel = binding.viewModel as BlogViewModel
//
    //    blogViewModel.activity = activity!!
    //    blogViewModel.context = context!!
//
    //    val user = FirebaseAuth.getInstance().currentUser
    //    blogViewModel.userId = user!!.uid
//
//
    //    return binding.root
    //}
//
    //override fun onActivityCreated(savedInstanceState: Bundle?) {
    //    super.onActivityCreated(savedInstanceState)
//
//
    //    val confirmFab = activity!!.findViewById<FloatingActionButton>(R.id.confirmFab)
    //    confirmFab.show()
//
    //    val liveData = blogViewModel.changeFragmentLiveData
    //    liveData.observe(viewLifecycleOwner, Observer {
    //        if (liveData.value == true){
    //            val bundle = Bundle()
    //            bundle.putString("title", blogViewModel.blog.title)
    //            bundle.putString("blogId", blogViewModel.blog.blogId)
    //            bundle.putString("ownerId", blogViewModel.blog.ownerId)
    //            bundle.putStringArrayList("time", blogViewModel.blog.time)
    //            bundle.putStringArrayList("messages", blogViewModel.blog.messages)
//
    //            val fragment = ChatFragment.newInstance()
    //            fragment.arguments = bundle
//
    //            liveData.value = false
    //        }
    //    })
//
    //    val blogPic: de.hdodenhof.circleimageview.CircleImageView  = activity!!.findViewById(R.id.blogPic)
    //    //blogPic.setImageResource(R.drawable.camera)
    //    blogPic.setOnClickListener {
    //        if(ActivityCompat.checkSelfPermission(activity!!,
    //                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
    //        {
    //            requestPermissions(
    //                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
    //                2000)
    //        }
    //        else {
    //            startGallery()
    //        }
    //    }
    //}
//
    //private fun startGallery() {
    //    val cameraIntent =
    //        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    //    cameraIntent.type = "image/*"
    //    if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
    //        startActivityForResult(cameraIntent, 1000)
    //    }
    //}
//
    //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    //{
    //    if (resultCode == Activity.RESULT_OK) {
    //        if (requestCode == 1000) {
    //            val returnUri: Uri? = data.data
    //            val bitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
//
    //            val blogPic: de.hdodenhof.circleimageview.CircleImageView  = activity!!.findViewById(R.id.blogPic)
    //            blogPic.setImageBitmap(bitmapImage)
//
    //            blogViewModel.bitmapImage = bitmapImage
    //        }
    //    }
    //}
}
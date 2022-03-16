package com.satulima.pangan.ui.auth.setup

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mazenrashed.dotsindicator.DotsIndicator
import com.satulima.pangan.R
import com.satulima.pangan.databinding.FragmentAccountSetup3Binding
import com.satulima.pangan.entity.User
import com.satulima.pangan.service.Status
import com.satulima.pangan.ui.auth.AuthViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AccountSetup3Fragment : Fragment() {

    private var _binding : FragmentAccountSetup3Binding? = null
    private val binding get() = _binding!!
    private val args: AccountSetup3FragmentArgs by navArgs()
    private lateinit var newUser : User
    private val authViewModel : AuthViewModel by viewModels()
    private var photoUri: Uri? = null
    val CAMERA_REQUEST_CODE = 102
    val GALLERY_REQUEST_CODE = 105


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountSetup3Binding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()
        newUser = args.newUser
        binding.editTextUsername.setText(newUser.username)
        if (newUser.profilePicture.isNotBlank()){
            Picasso.get().load(newUser.profilePicture).into(binding.imageViewProfPict)
        }

        activity?.findViewById<DotsIndicator>(R.id.dotIndicatorAccountSetup)?.setDotSelection(if (args.isByGoogle) 1 else 2)

        binding.buttonRlPrev.setOnClickListener {
            updateNewUser()
            val action = AccountSetup3FragmentDirections.setup3ToSetup2(args.isByGoogle, newUser, args.googleAccount)
            Navigation.findNavController(it).navigate(action)
        }
        binding.buttonRlRegister.setOnClickListener {
            if (binding.editTextUsername.text.isNullOrBlank()){
                binding.inputLayoutUsername.error = "Username can't be empty"
                binding.inputLayoutUsername.isErrorEnabled = true
                binding.inputLayoutUsername.requestFocus()
            }else{
                binding.inputLayoutUsername.isErrorEnabled = false
                if (photoUri != null  && photoUri.toString().isNotBlank()){
                    uploadPhoto()
                }
                if (args.isByGoogle){
                    binding.buttonRlRegister.visibility = View.INVISIBLE
                    val credential = GoogleAuthProvider.getCredential(args.googleAccount.idToken, null)
                    val firebaseAuth = FirebaseAuth.getInstance()
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            updateNewUser()
                            newUser.profilePicture = args.googleAccount.photoUrl.toString()
                            Toast.makeText(requireContext(), "Register Success : $newUser", Toast.LENGTH_SHORT).show()
                            firebaseAuth.signOut()
                        }else{
                            Toast.makeText(requireContext(), task.exception?.message.toString(), Toast.LENGTH_SHORT)
                        }
                        binding.buttonRlRegister.visibility = View.VISIBLE
                    }
                }else{
                    registerByEmail(it)
                }
            }
        }
        binding.buttonAddProfPict.setOnClickListener {
            selectImage()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    updateNewUser()
                    val action = AccountSetup3FragmentDirections.setup3ToSetup2(args.isByGoogle, newUser, args.googleAccount)
                    Navigation.findNavController(binding.root).navigate(action)
                }
            })

    }

    private fun updateNewUser(){
        newUser.username = binding.editTextUsername.text.toString()
    }

    private fun uploadPhoto(){
        authViewModel.uploadPhoto(photoUri!!, "${newUser.email}_${binding.editTextUsername.text.toString()}")
        lifecycleScope.launch {
            authViewModel.uploadPhotoState.collect{
                when(it.status){
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            newUser.profilePicture = data
                        }
                    }
                    Status.ERROR -> {
                        it.message?.let {msg ->
                            Toast.makeText(requireContext(), "Register Failed : $msg", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun registerByEmail(view: View){
        authViewModel.registerWithEmail(newUser.email, newUser.password)
        lifecycleScope.launch {
            authViewModel.registerWithEmailState.collect {
                when(it.status) {
                        Status.LOADING -> {
                            binding.progressBarRegisterEmail.visibility = View.VISIBLE
                            binding.buttonRlRegister.visibility = View.INVISIBLE
                        }
                        Status.SUCCESS -> {
                            binding.progressBarRegisterEmail.visibility = View.INVISIBLE
                            binding.buttonRlRegister.visibility = View.VISIBLE
                            it.data?.let{data ->
                                newUser.email = data.email.toString()
                                newUser.firstName = data.displayName.toString()
                                newUser.lastName = ""
                            }
                            Toast.makeText(requireContext(), "Register Success $newUser", Toast.LENGTH_SHORT).show()
                        }
                    else -> {
                        binding.progressBarRegisterEmail.visibility = View.INVISIBLE
                        binding.buttonRlRegister.visibility = View.VISIBLE
                            it.message?.let {msg ->
                                Toast.makeText(
                                    context,
                                    "Failed: $msg",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun selectImage(){
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo")
        builder.setItems(options){dialog, item ->
            when(item){
                0 -> {
                    openCamera()
                }
                1 -> {
                    openGallery()
                }
                2 -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null){
            when(requestCode){
                CAMERA_REQUEST_CODE -> {
                    data.extras?.let {
                        val bitmap = it.get("data") as Bitmap
                        binding.imageViewProfPict.setImageBitmap(bitmap)
                        photoUri = getImageUri(requireContext(), bitmap)
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    var contentUri = Uri.EMPTY
                    data.data?.let {
                        contentUri = it
                        photoUri = it
                    }
                    binding.imageViewProfPict.setImageURI(contentUri)
                }
            }
        }
    }

    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


}
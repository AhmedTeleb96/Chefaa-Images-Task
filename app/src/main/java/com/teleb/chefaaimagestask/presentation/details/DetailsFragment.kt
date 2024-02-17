package com.teleb.chefaaimagestask.presentation.details

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.teleb.chefaaimagestask.R
import com.teleb.chefaaimagestask.data.utils.InternetConnectionLiveData
import com.teleb.chefaaimagestask.databinding.FragmentDetailsBinding
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.ThumbnailEntity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class DetailsFragment : Fragment() {


    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailsBinding


    private var imageUriSelected: Uri? = null

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                imageUriSelected = data?.data
                binding.ivCharacterImg.setImageURI(imageUriSelected!!)
            }
        }
    private var isInternetAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        InternetConnectionLiveData.getInstance(activity as AppCompatActivity)
            .observe(viewLifecycleOwner) {
                isInternetAvailable = it
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.loading.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            saveBtn.setOnClickListener {
                ilWidth.error = null
                detailLayoutHeight.error = null
                ilCaption.error = null
                val title = binding.etCaption.text.toString()
                    .ifEmpty {
                        ilCaption.error = " "
                        ""
                    }
                val character = args.character.apply { this?.name = title } ?: CharacterEntity(
                    Random().nextInt(1000),
                    thumbnail = ThumbnailEntity("", "", ""), name = title
                )
                if (validInput() && (imageUriSelected != null || character.thumbnail.inInitialState()))
                    viewModel.resizeImageWithValidation(
                        character,
                        etWidth.text.toString(),
                        etHeight.text.toString(), imageUriSelected,
                        isInternetAvailable
                    )
                else
                    Toast.makeText(activity,"fields not complete or no image exist",Toast.LENGTH_SHORT).show()
            }
            ivCharacterImg.setOnClickListener {
                openGallery()
            }
        }

        viewModel.character.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.etCaption.setText(it.name)
            binding.ivCharacterImg.setImageBitmap(it.thumbnail.imageBitmap)
            it.thumbnail.imageBitmap?.let { imageBitmap -> setDominantColor(imageBitmap) }
        }

        args.character?.let {
            binding.tvId.text = it.id.toString()
            binding.etCaption.setText(it.name.ifEmpty {
                binding.ilCaption.error = " "
                getString(R.string.no_caption)
            })
            binding.ivCharacterImg.setImageBitmap(it.thumbnail.imageBitmap)
            it.thumbnail.imageBitmap?.let { imageBitmap -> setDominantColor(imageBitmap) }
        }

        activity?.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

    }

    private fun validInput(): Boolean {
        if (binding.etCaption.text.toString().isEmpty()) return false
        if (binding.etWidth.text.toString().isEmpty()) return false
        if (binding.etHeight.text.toString().isEmpty()) return false
        return true
    }

    private fun setDominantColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val dominantColor = palette?.dominantSwatch?.rgb ?: 0
            binding.clMain.setBackgroundColor(dominantColor)
        }
    }

    private fun openGallery() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

}
package com.teleb.chefaaimagestask.presentation.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.teleb.chefaaimagestask.R
import com.teleb.chefaaimagestask.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {


    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.loading.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == null) return@observe
            Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
        }


        viewModel.character.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.etCaption.setText(it.name)
            binding.ivCharacterImg.setImageBitmap(it.thumbnail.imageBitmap)
            it.thumbnail.imageBitmap?.let { imageBitmap -> setDominantColor(imageBitmap) }
        }

        binding.saveBtn.setOnClickListener {

        }

        args.character?.let {
            binding.tvId.text = it.id.toString()
            binding.etCaption.setText(it.name.ifEmpty { getString(R.string.no_caption) })
            binding.ivCharacterImg.setImageBitmap(it.thumbnail.imageBitmap)
            it.thumbnail.imageBitmap?.let { imageBitmap -> setDominantColor(imageBitmap) }
        }

    }

    private fun setDominantColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val dominantColor = palette?.dominantSwatch?.rgb ?: 0
            binding.clMain.setBackgroundColor(dominantColor)
        }
    }
}
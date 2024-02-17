package com.teleb.chefaaimagestask.presentation.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teleb.chefaaimagestask.BuildConfig
import com.teleb.chefaaimagestask.R
import com.teleb.chefaaimagestask.databinding.FragmentHomeBinding
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.presentation.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var charactersAdapter: CharactersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charactersAdapter = CharactersAdapter()
        binding.rvCharacters.adapter = charactersAdapter

        charactersAdapter.onItemClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
        }
        charactersAdapter.onItemLongClickListener = { characterEntity , view ->
            showImagePopupMenu(view, characterEntity)
        }

        viewModel.characters.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.refresh.isRefreshing = false
            charactersAdapter.characters = it
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.refresh.isRefreshing = false
            binding.loading.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.refresh.isRefreshing = false
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        }

        binding.etSearchCaption.doOnTextChanged { text, _, _, _ ->
            charactersAdapter.searchByCaption(text.toString())
        }

        binding.addingCharacter.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(null))
        }

        binding.refresh.setOnRefreshListener {
            viewModel.getAllCharacters()
        }

        activity?.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun showImagePopupMenu(view: View, characterEntity: CharacterEntity) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.image_menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_share -> ImageUtils.shareImage(
                    requireContext(),
                    characterEntity.thumbnail.imageBitmap!!
                )

                R.id.action_save -> ImageUtils.saveImage(
                    characterEntity.thumbnail.imageBitmap!!
                ).let {
                    if (it) Toast.makeText(activity,"image saved successfully",Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
    override fun onResume() {
        super.onResume()
        viewModel.getAllCharacters()
    }
}
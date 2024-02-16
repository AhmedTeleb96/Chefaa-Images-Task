package com.teleb.chefaaimagestask.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teleb.chefaaimagestask.BuildConfig
import com.teleb.chefaaimagestask.R
import com.teleb.chefaaimagestask.databinding.FragmentHomeBinding
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
        charactersAdapter.onItemLongClickListener = {
            // TODO show popup
        }

        viewModel.characters.observe(viewLifecycleOwner){
            if (it == null) return@observe
            charactersAdapter.characters = it
        }

        viewModel.loading.observe(viewLifecycleOwner){
            if (it == null) return@observe
            binding.loading.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner){
            if (it == null) return@observe
            Log.i("zzz", "onViewCreated: $it")
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        }

        binding.etSearchCaption.doOnTextChanged { text, _, _, _ ->
            charactersAdapter.searchByCaption(text.toString())
        }

        binding.addingCharacter.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(null))
        }
    }

}
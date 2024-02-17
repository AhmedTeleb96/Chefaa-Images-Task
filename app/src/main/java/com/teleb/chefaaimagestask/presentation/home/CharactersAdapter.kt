package com.teleb.chefaaimagestask.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teleb.chefaaimagestask.databinding.ItemCharacterBinding
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    var onItemClickListener: ((CharacterEntity) -> Unit)? = null
    var onItemLongClickListener: ((CharacterEntity,View) -> Unit)? = null
    private var characterItems: List<CharacterEntity> = emptyList()

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(characterEntity: CharacterEntity) = binding.apply {

            tvCaption.text = characterEntity.name

            ivComicImage.setImageBitmap(characterEntity.thumbnail.imageBitmap)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(characterEntity)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(characterEntity,this.root)
                true
            }
        }
    }

    var characters: List<CharacterEntity>
        get() = differ.currentList
        set(value) {
            characterItems = value
            performSearch(searchQuery)
        }


    private val differCallBack = object : DiffUtil.ItemCallback<CharacterEntity>() {
        override fun areItemsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity) =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: CharacterEntity, newItem: CharacterEntity) =
            oldItem.id == newItem.id
    }

    private val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharactersViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val characterItem = characters[position]
        holder.bindData(characterItem)
    }

    private var searchQuery: String = ""

    fun searchByCaption(query: String) {
        searchQuery = query
        performSearch(query)
    }

    private fun performSearch(query: String) {
        val filteredList = if (query.isEmpty()) {
            characterItems
        } else {
            characterItems.filter { it.name.contains(query, true) }
        }
        differ.submitList(filteredList)
    }
}
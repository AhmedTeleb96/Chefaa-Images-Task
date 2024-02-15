package com.teleb.chefaaimagestask.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.usecases.GetAllCharactersUseCase
import com.teleb.chefaaimagestask.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllCharactersUseCase: GetAllCharactersUseCase) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _characters = MutableLiveData<List<CharacterEntity>?>()
    val characters: LiveData<List<CharacterEntity>?>
        get() = _characters

    private fun getAllCharacters() {
        viewModelScope.launch{
            getAllCharactersUseCase.invoke()
                .onStart {
                    _loading.postValue(true)
                }
                .onCompletion {
                    _loading.postValue(false)
                }
                .collect { resource ->
                when (resource) {
                    is Resource.Success -> _characters.postValue(resource.data)
                    is Resource.Failed -> _error.postValue(resource.message)
                }

            }
        }
    }

    init {
        getAllCharacters()
    }
}
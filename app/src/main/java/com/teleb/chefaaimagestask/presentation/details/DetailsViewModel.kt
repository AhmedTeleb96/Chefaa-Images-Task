package com.teleb.chefaaimagestask.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.usecases.GetCharacterByIdUseCase
import com.teleb.chefaaimagestask.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val getCharacterByIdUseCase: GetCharacterByIdUseCase) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _character = MutableLiveData<CharacterEntity?>()
    val character: LiveData<CharacterEntity?>
        get() = _character

     fun getCharacter(id: Int) {
        viewModelScope.launch {
            getCharacterByIdUseCase.invoke(id)
                .onStart {
                    _loading.postValue(true)
                }
                .onCompletion {
                    _loading.postValue(false)
                }
                .catch { e ->
                    _error.postValue(e.message)
                }
                .collect { character ->
                    _character.postValue(character)
                }
        }
    }

}
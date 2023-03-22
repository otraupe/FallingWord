package com.otraupe.fallingwords.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otraupe.fallingwords.data.repository.LocalPairingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localPairingRepository: LocalPairingRepository
    ): ViewModel() {

    val uiState: MutableLiveData<MainUiState> = MutableLiveData<MainUiState>()

    fun getNextPairing() {
        viewModelScope.launch {
            val correct = Random.nextBoolean()
            val pairing = localPairingRepository.getPairing(correct = correct)
            uiState.postValue(MainUiState(currentPairing = pairing, correct = correct))
        }
    }
}
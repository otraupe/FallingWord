package com.otraupe.fallingwords.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otraupe.fallingwords.data.model.response.ResponseType
import com.otraupe.fallingwords.data.model.result.Result
import com.otraupe.fallingwords.domain.usecase.PairingUseCase
import com.otraupe.fallingwords.domain.usecase.ResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
        private val pairingUseCase: PairingUseCase,
        private val resultUseCase: ResultUseCase
    ): ViewModel() {

    private var _currentPairingCorrect = true
    private var _currentTotalScore = 0L
    private var _currentTrial = 0    // posting identical results on elapse won't advance the game

    private val _resultFlow = MutableStateFlow<Result?>(null)
    val resultFlow: StateFlow<Result?> = _resultFlow.asStateFlow()

    private val _scoreFlow = MutableStateFlow(0L)
    val scoreFlow: StateFlow<Long> = _scoreFlow.asStateFlow()

    val uiState: MutableLiveData<MainUiState> = MutableLiveData<MainUiState>()

    fun resetScore() {
        _currentTotalScore = 0L
        _scoreFlow.value = _currentTotalScore
    }

    fun nextPairing() {
        viewModelScope.launch {
            _currentPairingCorrect = Random.nextBoolean()
            val pairing = pairingUseCase.getNextPairing(correct = _currentPairingCorrect)
            uiState.postValue(
                MainUiState(currentPairing = pairing, correct = _currentPairingCorrect)
            )
        }
    }

    fun response(responseType: ResponseType, responseMillisLeft: Long) {
        _currentTrial++
        viewModelScope.launch {
            val (result, totalScore) = resultUseCase.calculateResult(
                trial = _currentTrial,
                type = responseType,
                pairingCorrect = _currentPairingCorrect,
                millisLeft = responseMillisLeft,
                currentScore = _currentTotalScore
            )
            _currentTotalScore = totalScore
            _scoreFlow.value = _currentTotalScore
            _resultFlow.value = result
        }
    }
}
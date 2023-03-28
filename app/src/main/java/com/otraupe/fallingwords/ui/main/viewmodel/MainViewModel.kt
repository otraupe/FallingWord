package com.otraupe.fallingwords.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otraupe.fallingwords.data.model.response.ResponseType
import com.otraupe.fallingwords.data.model.result.Result
import com.otraupe.fallingwords.domain.usecase.PairingUseCase
import com.otraupe.fallingwords.domain.usecase.ResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val uiStateLiveData = MutableLiveData<MainUiState>()
    val resultLiveData = MutableLiveData<Result>()
    val scoreLiveData = MutableLiveData<Long>()

    fun resetScore() {
        _currentTotalScore = 0L
        scoreLiveData.postValue(_currentTotalScore)
    }

    fun nextPairing() {
        viewModelScope.launch {
            _currentPairingCorrect = Random.nextBoolean()
            val pairing = pairingUseCase.getNextPairing(correct = _currentPairingCorrect)
            uiStateLiveData.postValue(
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
            scoreLiveData.postValue(_currentTotalScore)
            resultLiveData.postValue(result)
        }
    }
}
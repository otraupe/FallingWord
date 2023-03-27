package com.otraupe.fallingwords.ui.main.viewmodel

import com.otraupe.fallingwords.data.model.pairing.Pairing

data class MainUiState(
    val currentPairing: Pairing,
    val correct: Boolean
)
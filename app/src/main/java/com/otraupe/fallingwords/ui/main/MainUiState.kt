package com.otraupe.fallingwords.ui.main

import com.otraupe.fallingwords.data.model.Pairing

data class MainUiState(
    val currentPairing: Pairing,
    val correct: Boolean
)
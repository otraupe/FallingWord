package com.otraupe.fallingwords.domain.usecase

import com.otraupe.fallingwords.data.model.pairing.Pairing
import com.otraupe.fallingwords.data.repository.LocalPairingRepository
import javax.inject.Inject

class PairingUseCase @Inject constructor(
    private val localPairingRepository: LocalPairingRepository
){
    fun getNextPairing(correct: Boolean): Pairing {
        return localPairingRepository.getPairing(correct = correct)
    }
}
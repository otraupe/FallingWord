package com.otraupe.fallingwords.domain.usecase

import com.otraupe.fallingwords.data.model.response.ResponseType
import com.otraupe.fallingwords.data.model.result.Result
import com.otraupe.fallingwords.data.model.result.ResultType
import javax.inject.Inject

class ResultUseCase @Inject constructor() {

    private val elapsedPenaltyMillis = 1000L

    fun calculateResult(
        trial: Int,
        type: ResponseType,
        pairingCorrect: Boolean,
        millisLeft: Long,
        currentScore: Long
    ): Pair<Result, Long> {

        var resultingScore = currentScore
        val result: Result =

        if (type == ResponseType.ELAPSED) {
            val penalty = elapsedPenaltyMillis.coerceAtMost(currentScore)
            resultingScore -= penalty
            Result(
                trial = trial,
                type = ResultType.ELAPSED,
                score = penalty
            )
        } else if (type.correct == pairingCorrect) {
            resultingScore += millisLeft
            Result(
                trial = trial,
                type = ResultType.SUCCESS,
                score = millisLeft
            )
        } else {
            val penalty = millisLeft.coerceAtMost(currentScore)
            resultingScore -= penalty
            Result(
                trial = trial,
                type = ResultType.FAILURE,
                score = penalty
            )
        }

        return Pair(result, resultingScore)
    }
}
package com.otraupe.fallingwords

import com.otraupe.fallingwords.data.model.response.ResponseType
import com.otraupe.fallingwords.data.model.result.ResultType
import com.otraupe.fallingwords.domain.usecase.ResultUseCase
import org.junit.Assert
import org.junit.Test

class ResultUnitTest {

    private val resultUseCase: ResultUseCase = ResultUseCase()

    // ResponseType.ELAPSED

    @Test
    fun ifResponseTypeElapsed_resultTypeIsElapsed() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.ELAPSED,
            pairingCorrect = false,
            millisLeft = 0,
            currentScore = 5000L
        )
        Assert.assertEquals(result.type, ResultType.ELAPSED)
    }

    @Test
    fun ifResponseTypeElapsed_andCurrentScoreAbove1000_penaltyIs1000() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.ELAPSED,
            pairingCorrect = false,
            millisLeft = 0,
            currentScore = 1001L
        )
        Assert.assertEquals(result.score, 1000)
    }

    @Test
    fun ifResponseTypeElapsed_andCurrentScoreAbove1000_totalScoreIs1000Less() {
        val currentScore = 1001L
        val (_, totalScore) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.ELAPSED,
            pairingCorrect = false,
            millisLeft = 0,
            currentScore = currentScore
        )
        Assert.assertEquals(totalScore, currentScore - 1000L)
    }

    @Test
    fun ifResponseTypeElapsed_penaltyIsMaxCurrentScore() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.ELAPSED,
            pairingCorrect = false,
            millisLeft = 0,
            currentScore = 500L
        )
        Assert.assertEquals(result.score, 500)
    }

    // ResponseType.CORRECT

    @Test
    fun ifResponseTypeCorrect_andPairingCorrect_resultTypeIsSuccess() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = true,
            millisLeft = 0,
            currentScore = 5000L
        )
        Assert.assertEquals(result.type, ResultType.SUCCESS)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingCorrect_scoreIsMillisLeft() {
        val millisLeft = 1234L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = true,
            millisLeft = millisLeft,
            currentScore = 5000L
        )
        Assert.assertEquals(result.score, millisLeft)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingCorrect_totalScoreIsCurrentScorePlusMillisLeft() {
        val millisLeft = 1234L
        val currentScore = 5000L
        val (_, totalScore) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = true,
            millisLeft = millisLeft,
            currentScore = currentScore
        )
        Assert.assertEquals(totalScore, currentScore + millisLeft)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingIncorrect_resultTypeIsFailure() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = false,
            millisLeft = 1234L,
            currentScore = 5000L
        )
        Assert.assertEquals(result.type, ResultType.FAILURE)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingIncorrect_andCurrentScoreAboveMillisLeft_penaltyIsMillisLeft() {
        val millisLeft = 5000L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = false,
            millisLeft = millisLeft,
            currentScore = 5001L
        )
        Assert.assertEquals(result.score, millisLeft)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingIncorrect_andCurrentScoreAboveMillisLeft_totalScoreIsCurrentScoreMinusMillisLeft() {
        val millisLeft = 5000L
        val currentScore = 5001L
        val (_, totalScore) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = false,
            millisLeft = millisLeft,
            currentScore = currentScore
        )
        Assert.assertEquals(totalScore, currentScore - millisLeft)
    }

    @Test
    fun ifResponseTypeCorrect_andPairingIncorrect_penaltyIsMaxCurrentScore() {
        val currentScore = 500L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.CORRECT,
            pairingCorrect = false,
            millisLeft = 5000,
            currentScore = currentScore
        )
        Assert.assertEquals(result.score, currentScore)
    }

    // ResponseType.WRONG

    @Test
    fun ifResponseTypeWrong_andPairingIncorrect_resultTypeIsSuccess() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = false,
            millisLeft = 0,
            currentScore = 5000L
        )
        Assert.assertEquals(result.type, ResultType.SUCCESS)
    }

    @Test
    fun ifResponseTypeWrong_andPairingIncorrect_scoreIsMillisLeft() {
        val millisLeft = 1234L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = false,
            millisLeft = millisLeft,
            currentScore = 5000L
        )
        Assert.assertEquals(result.score, millisLeft)
    }

    @Test
    fun ifResponseTypeWrong_andPairingIncorrect_totalScoreIsCurrentScorePlusMillisLeft() {
        val millisLeft = 1234L
        val currentScore = 5000L
        val (_, totalScore) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = false,
            millisLeft = millisLeft,
            currentScore = currentScore
        )
        Assert.assertEquals(totalScore, currentScore + millisLeft)
    }

    @Test
    fun ifResponseTypeWrong_andPairingCorrect_resultTypeIsFailure() {
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = true,
            millisLeft = 1234L,
            currentScore = 5000L
        )
        Assert.assertEquals(result.type, ResultType.FAILURE)
    }

    @Test
    fun ifResponseTypeWrong_andPairingCorrect_andCurrentScoreAboveMillisLeft_penaltyIsMillisLeft() {
        val millisLeft = 5000L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = true,
            millisLeft = millisLeft,
            currentScore = 5001L
        )
        Assert.assertEquals(result.score, millisLeft)
    }

    @Test
    fun ifResponseTypeWrong_andPairingCorrect_andCurrentScoreAboveMillisLeft_totalScoreIsCurrentScoreMinusMillisLeft() {
        val millisLeft = 5000L
        val currentScore = 5001L
        val (_, totalScore) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = true,
            millisLeft = millisLeft,
            currentScore = currentScore
        )
        Assert.assertEquals(totalScore, currentScore - millisLeft)
    }

    @Test
    fun ifResponseTypeWrong_andPairingCorrect_penaltyIsMaxCurrentScore() {
        val currentScore = 500L
        val (result, _) = resultUseCase.calculateResult(
            trial = 0,
            type = ResponseType.WRONG,
            pairingCorrect = true,
            millisLeft = 5000,
            currentScore = currentScore
        )
        Assert.assertEquals(result.score, currentScore)
    }
}
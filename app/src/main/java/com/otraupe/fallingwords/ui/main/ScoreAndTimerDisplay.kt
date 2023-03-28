package com.otraupe.fallingwords.ui.main

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.otraupe.fallingwords.R
import com.otraupe.fallingwords.data.model.result.ResultType
import com.otraupe.fallingwords.ui.main.viewmodel.MainViewModel
import com.otraupe.fallingwords.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TimerAndScoreDisplay(
    viewModel: MainViewModel,
    started: MutableState<Boolean>,
    fallingWordVisible: Boolean,
    countDownValue: Long,
    defaultTextSize: Dp,
    coroutineScope: CoroutineScope
) {
    val density = LocalDensity.current

    val lastResult by viewModel.resultLiveData.observeAsState()

    var scoreMillisText by rememberSaveable { mutableStateOf(0L.toString()) }

    //region Vibrator preparation
    val context = LocalContext.current
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    val vibrationEffectCorrect: VibrationEffect =
        VibrationEffect.createWaveform(longArrayOf(0L,100L),-1)
    val vibrationEffectIncorrect: VibrationEffect =
        VibrationEffect.createWaveform(longArrayOf(0L,500L),-1)
    val vibrationEffectElapsed: VibrationEffect =
        VibrationEffect.createWaveform(longArrayOf(0L,200L,100L,200L),-1)
    //endregion

    val feedbackDurationMillis = context.resources.getInteger(R.integer.default_response_feedback_millis)

    var successScoreVisible by rememberSaveable { mutableStateOf(false) }
    var failureScoreVisible by rememberSaveable { mutableStateOf(false) }

    //region Result handling
    LaunchedEffect(key1 = lastResult) {
        scoreMillisText = (lastResult?.score ?: 0L).toString()
        vibrator.cancel()
        when (lastResult?.type) {
            ResultType.SUCCESS -> {
                vibrator.vibrate(vibrationEffectCorrect)
                successScoreVisible = true
            }
            ResultType.FAILURE -> {
                vibrator.vibrate(vibrationEffectIncorrect)
                failureScoreVisible = true
            }
            ResultType.ELAPSED -> {
                vibrator.vibrate(vibrationEffectElapsed)
                failureScoreVisible = true
            }
            else -> {}
        }
        coroutineScope.launch {
            delay(feedbackDurationMillis.toLong())
            if (started.value) {    // stop button might get pressed during delay
                viewModel.nextPairing()
            }
        }
    }
    //endregion

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
    ) {
        if (fallingWordVisible) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = countDownValue.toString(),
                    color = ColorTextDefault,
                    fontSize = with(density) { defaultTextSize.toSp() }
                )
            }
        }
        ScoreText(
            text = scoreMillisText,
            defaultTextSize = defaultTextSize,
            visible = successScoreVisible,
            hiddenColor = ColorTextSuccessHidden,
            visibleColor = ColorTextSuccessVisible,
            animationDuration = feedbackDurationMillis
        ) {
            successScoreVisible = false
        }
        ScoreText(
            text = scoreMillisText,
            defaultTextSize = defaultTextSize,
            visible = failureScoreVisible,
            hiddenColor = ColorTextFailureHidden,
            visibleColor = ColorTextFailureVisible,
            animationDuration = feedbackDurationMillis
        ) {
            failureScoreVisible = false
        }
    }
}
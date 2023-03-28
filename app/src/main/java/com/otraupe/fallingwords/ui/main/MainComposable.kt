package com.otraupe.fallingwords.ui.main

import android.os.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otraupe.fallingwords.R
import com.otraupe.fallingwords.data.model.response.ResponseType
import com.otraupe.fallingwords.ui.dialog.InstructionsDialog
import com.otraupe.fallingwords.ui.main.viewmodel.MainViewModel
import com.otraupe.fallingwords.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainComposable() {
    val viewModel: MainViewModel = viewModel()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val started = rememberSaveable { mutableStateOf(false) }
    var showInstructionsDialog by rememberSaveable { mutableStateOf(false) }

    val uiState by viewModel.uiStateLiveData.observeAsState()
    val totalScore by viewModel.scoreLiveData.observeAsState()

    val density = LocalDensity.current
    val timerDurationMillis = context.resources.getInteger(R.integer.default_pairings_countdown_millis).toLong()

    var targetWord by rememberSaveable { mutableStateOf(context.resources.getString(R.string.ui_press_start)) }
    var currentTranslation by rememberSaveable { mutableStateOf("") }

    val fallingWordHeightDp = 48.dp
    val fallingWordHeightPx = with(density) { fallingWordHeightDp.toPx() }
    var playingFieldHeightPx by remember { mutableStateOf(0f) }
    var fallingWordVerticalOffset by remember { mutableStateOf(0) }
    var fallingWordVisible by rememberSaveable { mutableStateOf(false) }

    val defaultTextSize = 32.dp

    //region Timer and trial start
    var countDownValue by rememberSaveable { mutableStateOf(timerDurationMillis) }
    val countDownTimer = object : CountDownTimer(timerDurationMillis, 10) {
        override fun onTick(millisUntilFinished: Long) {
            countDownValue = millisUntilFinished
            val fractionLeft = millisUntilFinished.toFloat()/timerDurationMillis
            fallingWordVerticalOffset = - ((playingFieldHeightPx - fallingWordHeightPx) * fractionLeft).toInt()
            if (!fallingWordVisible) {  // cancel does not work from different thread, it seems
                cancel()
            }
        }
        override fun onFinish() {
            countDownValue = 0  // last tick is > 0
            fallingWordVisible = false
            viewModel.response(ResponseType.ELAPSED, 0)
        }
    }
    DisposableEffect(key1 = uiState) {
        targetWord = uiState?.currentPairing?.text_eng ?: context.resources.getString(R.string.ui_press_start)
        val translatedWord = uiState?.currentPairing?.text_spa
        if (!translatedWord.isNullOrBlank()) {
            scope.launch {
                delay(1000)
                currentTranslation = translatedWord
                fallingWordVisible = true
                countDownTimer.start()
            }
        }
        onDispose {
            fallingWordVisible = false
        }
    }
    //endregion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        ColorFalse,
                        ColorCorrect
                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //region Total Score
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            text = stringResource(id = R.string.ui_label_Score, totalScore ?: 0L),
            color = ColorTextDefault,
            textAlign = TextAlign.Center,
            fontSize = with(density) { defaultTextSize.toSp() }
        )
        //endregion

        //region Target word
        TargetWord(height = fallingWordHeightDp, word = targetWord)
        //endregion

        //region Timer/Score
        TimerAndScoreDisplay(
            viewModel = viewModel,
            started = started,
            fallingWordVisible = fallingWordVisible,
            countDownValue = countDownValue,
            defaultTextSize = defaultTextSize,
            coroutineScope = scope
        )
        //endregion

        //region Playing field
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    playingFieldHeightPx = coordinates.size.height.toFloat()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            FallingWord(
                word = currentTranslation,
                visible = fallingWordVisible,
                height = fallingWordHeightDp,
                verticalOffsetPx = fallingWordVerticalOffset,
                onResponseWrong = {
                    fallingWordVisible = false
                    viewModel.response(ResponseType.WRONG, countDownValue)
                },
                onResponseCorrect = {
                    fallingWordVisible = false
                    viewModel.response(ResponseType.CORRECT, countDownValue)
                }
            )
        }

        //region bottom delimiter
        Box(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        0.0f to ColorSeparatorStart,
                        0.5f to ColorSeparatorCenter,
                        1.0f to ColorSeparatorStart,
                        startX = 0f,
                        endX = Float.POSITIVE_INFINITY
                    )
                )
        )
        //endregion

        //endregion

        //region Buttons
        Button(
            modifier = Modifier
                .width(180.dp)
                .padding(vertical = 32.dp),
            onClick = {
                if (started.value) {
                    fallingWordVisible = false
                    started.value = false
                    viewModel.resetScore()
                    targetWord = context.resources.getString(R.string.ui_press_start)
                } else {
                    started.value = true
                    viewModel.nextPairing()
                }
            }
        ) {
            Text(
                text = if (started.value) stringResource(id = R.string.ui_buttons_stop)
                else stringResource(id = R.string.ui_buttons_next_pairing),
                fontSize = 18.sp
            )
        }
        //endregion

    }

    InstructionsDialog(showInstructionsDialog) { showInstructionsDialog = false}

    LaunchedEffect(Unit) {
        showInstructionsDialog = true
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FallingWordsTheme {
        MainComposable()
    }
}
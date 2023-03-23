package com.otraupe.fallingwords.ui.main

import android.os.CountDownTimer
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
import com.otraupe.fallingwords.ui.theme.*
import kotlin.math.ceil

@Composable
fun MainComposable() {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.observeAsState()

    var showInstructionsDialog by rememberSaveable { mutableStateOf(false) }

    val density = LocalDensity.current
    val context = LocalContext.current
    val timerDurationMillis =
        context.resources.getInteger(R.integer.default_pairings_countdown_millis).toLong()

    // TODO: animate timer (AnimateContent)

    var currentTranslation by rememberSaveable { mutableStateOf("") }
    var fallingWordVisible by rememberSaveable { mutableStateOf(false) }

    var playingFieldHeightPx by remember { mutableStateOf(0f) }
    var fallingWordVerticalOffset by remember { mutableStateOf(0) }

    val fallingWordHeightDp = 48.dp
    val fallingWordHeightPx = with(density) { fallingWordHeightDp.toPx() }

    var countDownValue by rememberSaveable { mutableStateOf(timerDurationMillis/1000) }
    val countDownTimer = object : CountDownTimer(timerDurationMillis, 10) {
        override fun onTick(millisUntilFinished: Long) {
            countDownValue = ceil(millisUntilFinished.toDouble()/1000).toLong()
            val fractionLeft = millisUntilFinished.toFloat()/timerDurationMillis
            fallingWordVerticalOffset = - ((playingFieldHeightPx - fallingWordHeightPx) * fractionLeft).toInt()
        }
        override fun onFinish() {
            countDownValue = 0
            fallingWordVisible = false
        }
    }
    DisposableEffect(key1 = uiState) {
        val translatedWord = uiState?.currentPairing?.text_spa
        if (translatedWord != null) {
            currentTranslation = translatedWord
            fallingWordVisible = true
            countDownTimer.start()
        }
        onDispose {
            countDownTimer.cancel()
        }
    }

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
        verticalArrangement = Arrangement.Center
    ) {

        //region Timer
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            text = countDownValue.toString(),
            textAlign = TextAlign.Center,
            fontSize = 32.sp
        )
        //endregion

        //region Target word
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            text = uiState?.currentPairing?.text_eng ?: "",
            textAlign = TextAlign.Center,
            fontSize = 32.sp
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
                verticalOffsetPx = fallingWordVerticalOffset
            )
        }

        //region bottom end of playing field
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
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 32.dp),
            onClick = { viewModel.getNextPairing() }
        ) {
            Text(text = stringResource(id = R.string.ui_buttons_next_pairing))
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
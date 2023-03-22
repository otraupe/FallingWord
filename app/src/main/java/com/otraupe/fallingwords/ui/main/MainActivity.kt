package com.otraupe.fallingwords.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otraupe.fallingwords.R
import com.otraupe.fallingwords.ui.theme.FallingWordsTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.ceil

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FallingWordsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainComposable()
                }
            }
        }
    }
}

@Composable
fun MainComposable() {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.observeAsState()

    val context = LocalContext.current
    val timerDurationMillis =
        context.resources.getInteger(R.integer.default_pairings_countdown_millies).toLong()

    // TODO: separate composables
    // TODO: animate timer (AnimateContent)

    var countDownValue by rememberSaveable { mutableStateOf(timerDurationMillis/1000) }
    val countDownTimer = object : CountDownTimer(timerDurationMillis, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Timber.d("Current countdown value is $millisUntilFinished")
            countDownValue = ceil(millisUntilFinished.toDouble()/1000).toLong()
        }
        override fun onFinish() {
            countDownValue = 0
        }
    }
    DisposableEffect(key1 = uiState) {
        countDownTimer.start()
        onDispose { // TODO: ???
            countDownTimer.cancel()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
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
            modifier = Modifier.fillMaxWidth(),
            text = uiState?.currentPairing?.text_eng ?: "",
            textAlign = TextAlign.Center,
            fontSize = 32.sp
        )
        //endregion

        //region Falling word
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = uiState?.currentPairing?.text_spa ?: "",
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            )
        }
        //endregion

        //region Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
            ) {
                Text(text = stringResource(id = R.string.ui_buttons_pairing_incorrect))
            }
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {  },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text(text = stringResource(id = R.string.ui_buttons_pairing_correct))
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 64.dp, end = 64.dp, top = 16.dp, bottom = 32.dp),
            onClick = { viewModel.getNextPairing() }
        ) {
            Text(text = stringResource(id = R.string.ui_buttons_next_pairing))
        }
        //endregion

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FallingWordsTheme {
        MainComposable()
    }
}
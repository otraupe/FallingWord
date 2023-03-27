package com.otraupe.fallingwords.ui.main

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otraupe.fallingwords.R
import com.otraupe.fallingwords.ui.theme.FallingWordsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FallingWord(
    word: String,
    visible: Boolean,
    height: Dp,
    verticalOffsetPx: Int,
    onResponseWrong: () -> Unit = {},
    onResponseCorrect: () -> Unit = {},
) {
    val config = LocalConfiguration.current
    val horizontalPadding = (config.screenWidthDp * .20f).dp

    var drawnWord by rememberSaveable { mutableStateOf(word) }
    val defaultFontSize = 36.sp
    val textStyleBodyLarge = MaterialTheme.typography.bodyLarge
    var textStyle by remember { mutableStateOf(textStyleBodyLarge.copy(fontSize = defaultFontSize)) }
    var readyToDraw by remember { mutableStateOf(false) }

    val animSpecFade: FiniteAnimationSpec<Float> = keyframes {
        this.durationMillis = 200
    }

    //region Dismiss state
    val configuration = LocalConfiguration.current
    val dismissThresholdInDp = (configuration.screenWidthDp * .25f).dp
    val dismissState = rememberDismissState(
        initialValue = DismissValue.Default,
        positionalThreshold = { _ -> dismissThresholdInDp.toPx() },
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToStart -> {
                    onResponseWrong()
                    true   // whether to confirm the dismiss
                }
                DismissValue.DismissedToEnd -> {
                    onResponseCorrect()
                    true
                }
                else -> false
            }
        }
    )
    //endregion

    // reset text size on new word
    LaunchedEffect(key1 = word) {
        if (word.isNotBlank()) {    // due to AnimatedVisibility, SwipeToDismiss is not initialized
                                    // on initial compose
            dismissState.snapTo(DismissValue.Default)
            textStyle = textStyleBodyLarge.copy(fontSize = defaultFontSize)
            readyToDraw = false
            drawnWord = word
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .offset { IntOffset(0, verticalOffsetPx) },
        visible = visible,
        enter = fadeIn(
            animationSpec = animSpecFade,
            initialAlpha = 0.0f
        ),
        exit = fadeOut(
            animationSpec = animSpecFade,
            targetAlpha = 0.0f
        )
    ) {

        //region Swipe to dismiss
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(
                DismissDirection.StartToEnd,
                DismissDirection.EndToStart
            ),
            background = {
            },
            dismissContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .height(height)
                        .background(shape = CircleShape, color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.ui_cd_response_left)
                    )
                    Text(
                        text = drawnWord,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = textStyle,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .drawWithContent {
                                if (readyToDraw) drawContent()
                            },
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.didOverflowWidth || textLayoutResult.didOverflowHeight) {
                                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                            } else {
                                readyToDraw = true
                            }
                        }
                    )
                    Image(
                        modifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.ui_cd_response_left)
                    )
                }
            }
        )
        //endregion

    }
}

@Preview(showBackground = false)
@Composable
fun FallingWordPreview() {
    FallingWordsTheme {
        FallingWord(
            word = "frase muy larga en español",
            visible = true,
            height = 48.dp,
            verticalOffsetPx = 0f.toInt(),
            onResponseWrong = {},
            onResponseCorrect = {}
        )
    }
}
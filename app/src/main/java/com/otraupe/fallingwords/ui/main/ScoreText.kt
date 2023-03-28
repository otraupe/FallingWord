package com.otraupe.fallingwords.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScoreText(
    text: String,
    defaultTextSize: Dp,
    visible: Boolean,
    hiddenColor: Color,
    visibleColor: Color,
    animationDuration: Int,
    onFinishedAnimation: () -> Unit
) {
    val density = LocalDensity.current

    val color by animateColorAsState(
        targetValue = when (visible) {
            false -> hiddenColor
            true -> visibleColor
        },
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        finishedListener = { onFinishedAnimation() }
    )
    val size: Dp by animateDpAsState(
        targetValue = when (visible) {
            false -> defaultTextSize
            true -> 64.dp
        },
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    if (visible) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = color,
                fontSize = with(density) { size.toSp() },
                fontWeight = FontWeight.Bold
            )
        }
    }
}
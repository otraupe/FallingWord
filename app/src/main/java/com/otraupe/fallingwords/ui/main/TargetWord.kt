package com.otraupe.fallingwords.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.otraupe.fallingwords.R

@Composable
fun TargetWord(height: Dp, word: String) {

    val context = LocalContext.current
    val startString = context.resources.getString(R.string.ui_press_start)

    var fieldA by rememberSaveable { mutableStateOf(false) }
    var fieldAText by rememberSaveable { mutableStateOf(startString) }
    var fieldBText by rememberSaveable { mutableStateOf(startString) }
    var zIndexA by rememberSaveable { mutableStateOf(-1f) }
    var zIndexb by rememberSaveable { mutableStateOf(-1f) }

    val animSpecFade: FiniteAnimationSpec<Float> = tween(750)

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {

        //region Field A
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(zIndexA),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = fieldA,
                enter = fadeIn(
                    animationSpec = animSpecFade,
                    initialAlpha = 0.0f
                ),
                exit = fadeOut(
                    animationSpec = animSpecFade,
                    targetAlpha = 0.0f
                )
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(height)
                        .background(shape = CircleShape, color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                        text = fieldAText,
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                }
            }
        }
        //endregion

        //region Field B
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(zIndexb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = !fieldA,
                enter = fadeIn(
                    animationSpec = animSpecFade,
                    initialAlpha = 0.0f
                ),
                exit = fadeOut(
                    animationSpec = animSpecFade,
                    targetAlpha = 0.0f
                )
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(height)
                        .background(shape = CircleShape, color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                        text = fieldBText,
                        color = Color.Black,
                        fontSize = 24.sp
                    )
                }
            }
        }
        //endregion

    }

    LaunchedEffect(key1 = word) {
        fieldA = !fieldA
        if (fieldA) {
            zIndexA = 1f
            zIndexb = -1f
            fieldAText = word
        } else {
            zIndexA = -1f
            zIndexb = 1f
            fieldBText = word
        }
    }
}
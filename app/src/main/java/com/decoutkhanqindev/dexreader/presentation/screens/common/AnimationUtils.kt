package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.animateItemOnAppear(): Modifier {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    
    val transition = updateTransition(visibleState, label = "ItemAppearance")
    
    val alpha by transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "Alpha"
    ) { if (it) 1f else 0f }
    
    val scale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow) },
        label = "Scale"
    ) { if (it) 1f else 0.8f }
    
    val translationY by transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = "TranslationY"
    ) { if (it) 0f else 50f }

    return this.graphicsLayer {
        this.alpha = alpha
        this.scaleX = scale
        this.scaleY = scale
        this.translationY = translationY
    }
}

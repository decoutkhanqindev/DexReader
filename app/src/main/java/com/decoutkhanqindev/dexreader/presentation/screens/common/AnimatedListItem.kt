package com.decoutkhanqindev.dexreader.presentation.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedListItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.animateItemOnAppear()) {
        content()
    }
}

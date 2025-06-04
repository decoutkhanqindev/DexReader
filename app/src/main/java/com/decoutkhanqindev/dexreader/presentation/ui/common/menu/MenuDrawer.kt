package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuDrawer(
  drawerState: DrawerState,
  items: List<MenuItem>,
  selectedItemId: String,
  onItemClick: (String) -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        Column(modifier = Modifier.fillMaxSize()) {
          MenuHeader(
            modifier = Modifier
              .weight(0.4f)
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surfaceContainer)
              .padding(16.dp)
          )
          MenuBody(
            items = items,
            selectedItemId = selectedItemId,
            onItemClick = onItemClick,
            modifier = Modifier
              .weight(2f)
              .fillMaxWidth()
          )
          MenuFooter(
            modifier = Modifier
              .weight(0.2f)
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surface)
          )
        }
      }
    },
    content = content,
    modifier = modifier
  )
}
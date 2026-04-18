package com.decoutkhanqindev.dexreader.presentation.screens.common.menu


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
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.model.value.menu.MenuValue
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MenuDrawer(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  drawerState: DrawerState,
  selectedItem: MenuValue,
  modifier: Modifier = Modifier,
  onSignInClick: () -> Unit,
  onItemClick: (MenuValue) -> Unit,
  content: @Composable () -> Unit,
) {
  ModalNavigationDrawer(
    drawerState = drawerState,
    modifier = modifier,
    drawerContent = {
      ModalDrawerSheet {
        Column(modifier = Modifier.fillMaxSize()) {
          MenuHeader(
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            modifier = Modifier
              .weight(0.4f)
              .fillMaxWidth()
              .padding(16.dp),
          ) { onSignInClick() }

          MenuBody(
            selectedItem = selectedItem,
            items = MenuValue.entries.toPersistentList(),
            modifier = Modifier
              .weight(2f)
              .fillMaxWidth(),
          ) { onItemClick(it) }

          MenuFooter(
            modifier = Modifier
              .weight(0.2f)
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surface)
          )
        }
      }
    }
  ) { content() }
}
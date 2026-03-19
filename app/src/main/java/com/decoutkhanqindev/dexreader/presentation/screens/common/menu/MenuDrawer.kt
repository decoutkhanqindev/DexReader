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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.value.menu.MenuItemValue
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MenuDrawer(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  onSignInClick: () -> Unit,
  drawerState: DrawerState,
  selectedItem: MenuItemValue,
  onItemClick: (MenuItemValue) -> Unit,
  content: @Composable () -> Unit,
  modifier: Modifier = Modifier,
) {
  val items = remember { MenuItemValue.entries.toPersistentList() }

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet {
        Column(modifier = Modifier.fillMaxSize()) {
          MenuHeader(
            isUserLoggedIn = isUserLoggedIn,
            currentUser = currentUser,
            onSignInClick = onSignInClick,
            modifier = Modifier
              .weight(0.4f)
              .fillMaxWidth()
              .padding(16.dp)
          )
          MenuBody(
            items = items,
            selectedItem = selectedItem,
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
    content = { content() },
    modifier = modifier
  )
}
package com.decoutkhanqindev.dexreader.presentation.screens.common.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.model.user.UserModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.ActionButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.PersonPicture
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.ProfilePicture
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MenuHeader(
  isUserLoggedIn: Boolean,
  currentUser: UserModel?,
  modifier: Modifier = Modifier,
  onSignInClick: () -> Unit,
) {
  if (isUserLoggedIn) {
    Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (currentUser?.avatarUrl != null) {
        ProfilePicture(
          url = currentUser.avatarUrl,
          name = currentUser.name,
          modifier = Modifier.weight(0.25f)
        )
      } else {
        PersonPicture(modifier = Modifier.weight(0.25f))
      }

      Column(
        modifier = Modifier
          .weight(0.75f)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = currentUser?.name ?: "",
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleLarge,
        )
        Text(
          text = currentUser?.email?.substringBefore("@gmail.com") ?: "",
          modifier = Modifier.fillMaxWidth(),
          fontStyle = FontStyle.Italic,
          fontWeight = FontWeight.Light,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }
  } else {
    Box(
      modifier = modifier,
      contentAlignment = Alignment.Center
    ) {
      ActionButton(
        backgroundColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.wrapContentSize(),
        onClick = onSignInClick,
      ) {
        Text(
          text = stringResource(R.string.sign_in),
          color = MaterialTheme.colorScheme.onPrimary,
          fontWeight = FontWeight.ExtraBold,
          style = MaterialTheme.typography.titleMedium,
        )

        Icon(
          imageVector = Icons.AutoMirrored.Filled.Login,
          contentDescription = stringResource(R.string.sign_in),
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier
            .size(24.dp)
            .padding(start = 8.dp)
        )
      }
    }
  }
}

@Preview
@Composable
private fun MenuHeaderLoggedInWithAvatarPreview() {
  DexReaderTheme {
    MenuHeader(
      isUserLoggedIn = true,
      currentUser = UserModel(
        id = "user-001",
        name = "Nguyen Van A",
        email = "nguyenvana@gmail.com",
        avatarUrl = "https://example.com/avatar.jpg"
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
      onSignInClick = {}
    )
  }
}

@Preview
@Composable
private fun MenuHeaderLoggedInNoAvatarPreview() {
  DexReaderTheme {
    MenuHeader(
      isUserLoggedIn = true,
      currentUser = UserModel(
        id = "user-001",
        name = "Nguyen Van A",
        email = "nguyenvana@gmail.com",
        avatarUrl = null
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
      onSignInClick = {}
    )
  }
}

@Preview
@Composable
private fun MenuHeaderLoggedOutPreview() {
  DexReaderTheme {
    MenuHeader(
      isUserLoggedIn = false,
      currentUser = null,
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
      onSignInClick = {}
    )
  }
}
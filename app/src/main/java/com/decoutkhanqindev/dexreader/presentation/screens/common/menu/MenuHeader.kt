package com.decoutkhanqindev.dexreader.presentation.screens.common.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.screens.common.buttons.SubmitButton
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.PersonPicture
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.ProfilePicture

@Composable
fun MenuHeader(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  modifier: Modifier = Modifier,
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
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
        )
        Text(
          text = currentUser?.email?.substringBefore("@gmail.com") ?: "",
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Light,
          fontStyle = FontStyle.Italic,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  } else {
    SubmitButton(
      title = stringResource(R.string.sign_in),
      onSubmitClick = onSignInClick,
      modifier = modifier.wrapContentSize()
    )
  }
}
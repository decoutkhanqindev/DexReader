package com.decoutkhanqindev.dexreader.presentation.ui.common.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.presentation.ui.common.buttons.SubmitButton
import com.decoutkhanqindev.dexreader.presentation.ui.common.image.PersonImage

@Composable
fun MenuHeader(
  isUserLoggedIn: Boolean,
  currentUser: User?,
  onSignInClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (isUserLoggedIn) {
    Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (currentUser?.profilePictureUrl != null) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(currentUser.profilePictureUrl)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
          contentDescription = currentUser.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .weight(0.25f)
            .clip(CircleShape)
            .size(80.dp)
            .background(MaterialTheme.colorScheme.primary)
        )
      } else {
        PersonImage(modifier = Modifier.weight(0.25f))
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
      modifier = modifier.width(100.dp)
    )
  }
}
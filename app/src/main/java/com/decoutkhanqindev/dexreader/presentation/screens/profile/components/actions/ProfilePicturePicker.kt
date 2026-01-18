package com.decoutkhanqindev.dexreader.presentation.screens.profile.components.actions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.PersonPicture
import com.decoutkhanqindev.dexreader.presentation.screens.common.image.ProfilePicture

@Composable
fun ProfilePicturePicker(
  url: String?,
  name: String,
  onSelectedImageUrl: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { uri -> uri?.let { onSelectedImageUrl(it.toString()) } }
  )

  Box(
    modifier = modifier.clip(CircleShape),
    contentAlignment = Alignment.Center
  ) {
    if (url != null) {
      ProfilePicture(
        url = url,
        name = name,
      )
    } else PersonPicture()
    Row(
      modifier = Modifier
        .width(80.dp)
        .height(30.dp)
        .align(Alignment.BottomCenter)
        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      IconButton(
        onClick = {
          singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(
              ActivityResultContracts.PickVisualMedia.ImageOnly
            )
          )
        },
      ) {
        Icon(
          imageVector = Icons.Default.Image,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
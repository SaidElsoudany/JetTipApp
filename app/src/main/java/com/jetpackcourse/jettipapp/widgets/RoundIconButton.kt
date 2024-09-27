package com.jetpackcourse.jettipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.Person,
    onClick: () -> Unit = {},
    tint: Color = Color.Black.copy(alpha = 0.8f),
    backGroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable { onClick() }
            .then(other = Modifier.size(40.dp)),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backGroundColor),
        elevation = cardElevation(defaultElevation = elevation),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                modifier = Modifier.align(alignment = Alignment.Center),
                imageVector = imageVector, contentDescription = "Plus or Minus Icon",
                tint = tint
            )
        }
    }
}
package com.app.co_opilot.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FloatingPopup(
    from: String,
    text: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it - 80 } // comes from slightly higher
        ) + fadeIn() + scaleIn(),
        exit = slideOutVertically(
            targetOffsetY = { -it - 80 }
        ) + fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(18.dp),
                        clip = false
                    )
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF1F1F1F).copy(alpha = 0.95f))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        from,
                        color = Color(0xFFBEBEBE),
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }

    // Auto dismiss
    LaunchedEffect(visible) {
        if (visible) {
            delay(2500)
            onDismiss()
        }
    }
}

package teamevence.evenceapp.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Bar(text, modifier = modifier)
}

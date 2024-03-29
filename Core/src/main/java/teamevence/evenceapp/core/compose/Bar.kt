package teamevence.evenceapp.core.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Bar(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White
) {
    Row(
        modifier = modifier
//            .background(Mat)
            .height(60.dp)
            .fillMaxWidth(1f)
            .offset(10.dp)

    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
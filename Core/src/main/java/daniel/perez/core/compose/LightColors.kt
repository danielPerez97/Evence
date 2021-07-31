package daniel.perez.core.compose

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Primary = Color(android.graphics.Color.parseColor("#2196F3"))
private val PrimaryVariant = Color(android.graphics.Color.parseColor("#263238"))
private val OnPrimary = Color.White
private val Secondary = Color(android.graphics.Color.parseColor("#263238"))
private val SecondaryVariant = Color(android.graphics.Color.parseColor("#2196F3"))
private val OnSecondary = Color(android.graphics.Color.parseColor("#2196F3"))

val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    onPrimary = OnPrimary,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
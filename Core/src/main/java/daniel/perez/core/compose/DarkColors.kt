package daniel.perez.core.compose

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

private val Primary = Color(android.graphics.Color.parseColor("#263238"))
private val PrimaryVariant = Color(android.graphics.Color.parseColor("#263238"))
private val Secondary = Color(android.graphics.Color.parseColor("#263238"))
private val SecondaryVariant = Secondary
private val Background = Color(android.graphics.Color.parseColor("#37474F"))
private val Surface = Color(android.graphics.Color.parseColor("#263238"))
private val OnPrimary = Color.White
private val OnSecondary = Color(android.graphics.Color.parseColor("#2196F3"))

val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    secondaryVariant = SecondaryVariant,
    background = Background,
    surface = Surface,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
)
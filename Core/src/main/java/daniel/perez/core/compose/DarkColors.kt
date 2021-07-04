package daniel.perez.core.compose

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

private val Primary = Color(android.graphics.Color.parseColor("#2196F3"))
private val PrimaryVariant = Color(android.graphics.Color.parseColor("#263238"))
private val OnPrimary = Color.White
private val Secondary = Color(android.graphics.Color.parseColor("#263238"))
private val SecondaryVariant = Color(android.graphics.Color.parseColor("#263238"))
private val OnSecondary = Color(android.graphics.Color.parseColor("#2196F3"))

val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    onPrimary = OnPrimary,
    secondary = Secondary,
    secondaryVariant = SecondaryVariant,
    onSecondary = OnSecondary
)
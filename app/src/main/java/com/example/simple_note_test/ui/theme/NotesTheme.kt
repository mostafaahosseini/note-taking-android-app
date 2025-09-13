package com.example.simple_note_test.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

val Primary = Color(0xFF504EC3)
val Secondary = Color(0xFFDEDC52)
val Background = Color(0xFFFAF8FC)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF180E25)
val GreyDark = Color(0xFF827D89)
val GreyBase = Color(0xFFC8C5CB)
val GreyLight = Color(0xFFEFEEF0)
val Error = Color(0xFFCE3A54)

val Inter = FontFamily.Default

val NotesTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 29.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.sp
    )
)

val NotesShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable
fun NotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(
        primary = Primary,
        secondary = Secondary,
        background = Background,
        error = Error
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = NotesTypography,
        shapes = NotesShapes,
        content = content
    )
}
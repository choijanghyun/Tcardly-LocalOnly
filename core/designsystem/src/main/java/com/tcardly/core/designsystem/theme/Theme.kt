package com.tcardly.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TCardlyColors.TealDeep,
    onPrimary = TCardlyColors.PureWhite,
    primaryContainer = TCardlyColors.TealSoft,
    onPrimaryContainer = TCardlyColors.TealDeep,
    secondary = TCardlyColors.TealMid,
    onSecondary = TCardlyColors.PureWhite,
    background = TCardlyColors.CloudBase,
    onBackground = TCardlyColors.SlateDeep,
    surface = TCardlyColors.PureWhite,
    onSurface = TCardlyColors.SlateDeep,
    surfaceVariant = TCardlyColors.CloudBase,
    onSurfaceVariant = TCardlyColors.SlateMid,
    outline = TCardlyColors.BorderSoft,
    error = TCardlyColors.CoralWarm,
    onError = TCardlyColors.PureWhite,
    tertiary = TCardlyColors.Sky,
)

private val DarkColorScheme = darkColorScheme(
    primary = TCardlyColors.TealMid,
    onPrimary = TCardlyColors.DarkBackground,
    primaryContainer = TCardlyColors.TealDeep,
    onPrimaryContainer = TCardlyColors.TealSoft,
    secondary = TCardlyColors.TealMid,
    onSecondary = TCardlyColors.DarkBackground,
    background = TCardlyColors.DarkBackground,
    onBackground = TCardlyColors.PureWhite,
    surface = TCardlyColors.DarkSurface,
    onSurface = TCardlyColors.PureWhite,
    surfaceVariant = TCardlyColors.DarkCard,
    onSurfaceVariant = TCardlyColors.SlateLight,
    outline = TCardlyColors.DarkCard,
    error = TCardlyColors.CoralWarm,
    onError = TCardlyColors.PureWhite,
    tertiary = TCardlyColors.Sky,
)

@Composable
fun TCardlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as? Activity)?.window?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TCardlyTypography,
        content = content
    )
}

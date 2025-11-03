package org.schabi.newpipe.ui.components.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import org.schabi.newpipe.ui.theme.AppTheme

/**
 * A wrapper composable that applies the [AppTheme] and a [Surface] to its content.
 *
 * This provides a correctly themed root, ensuring background and content colors
 * are applied according to the current light or dark theme.
 *
 * @param content The composable content to be displayed within the themed surface.
 */
@Composable
fun AppThemeWithSurface(
    content: @Composable () -> Unit,
) {
    AppTheme {
        // Use surface so Material uses appropriate theme colours.
        Surface {
            content()
        }
    }
}

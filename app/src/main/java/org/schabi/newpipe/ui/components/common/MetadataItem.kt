package org.schabi.newpipe.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun MetadataItem(label: String, value: String) {
    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
        Text(text = "${label.uppercase()}: ", style = MaterialTheme.typography.labelLarge)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

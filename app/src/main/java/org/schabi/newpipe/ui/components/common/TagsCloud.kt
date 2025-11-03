package org.schabi.newpipe.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsCloud(label: String, tags: List<String>, onTagClick: (String) -> Unit = {}) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                FilterChip(
                    onClick = {
                        onTagClick(tag)
                    },
                    label = { Text(tag) },
                    selected = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun TagsCloudPreview() {
    AppThemeWithSurface {
        TagsCloud(
            label = "Tags",
            tags = listOf(
                "tag 1", "another tag", "a very very very long tag", "short", "music video", "official", "live"
            )
        )
    }
}

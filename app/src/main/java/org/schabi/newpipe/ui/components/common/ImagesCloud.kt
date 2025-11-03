package org.schabi.newpipe.ui.components.common

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.schabi.newpipe.R
import org.schabi.newpipe.extractor.Image
import org.schabi.newpipe.extractor.Image.ResolutionLevel
import org.schabi.newpipe.util.image.ImageStrategy

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImagesCloud(label: String, images: List<Image>, onImageClick: (String) -> Unit = {}) {
    val preferredImageUrl: String? = ImageStrategy.choosePreferredImage(images)
    val context = LocalContext.current

    Column {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            images.forEach { image ->
                val dimension = image.dimension(context)
                TextButton(contentPadding = PaddingValues(0.dp), onClick = { onImageClick(image.url) }) {
                    Text(
                        text = dimension,
                        fontWeight = if (image.url == preferredImageUrl) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}

fun Image.dimension(context: Context): String {
    val hasNoDimensionInfo = (height == Image.HEIGHT_UNKNOWN && width == Image.WIDTH_UNKNOWN) &&
        estimatedResolutionLevel != ResolutionLevel.UNKNOWN

    return if (!hasNoDimensionInfo) {
        val heightStr =
            height.takeIf { it >= 0 }?.toString() ?: context.getString(R.string.question_mark)
        val widthStr =
            width.takeIf { it >= 0 }?.toString() ?: context.getString(R.string.question_mark)
        // TODO: Use a template string "%sx%s"?
        "$heightStr x $widthStr"
    } else {
        when (estimatedResolutionLevel) {
            ResolutionLevel.LOW -> context.getString(R.string.image_quality_low)
            ResolutionLevel.MEDIUM -> context.getString(R.string.image_quality_medium)
            ResolutionLevel.HIGH -> context.getString(R.string.image_quality_high)
            else -> ""
        }
    }
}

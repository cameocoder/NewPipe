package org.schabi.newpipe.fragments.list.channel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.schabi.newpipe.R
import org.schabi.newpipe.extractor.channel.ChannelExtractor
import org.schabi.newpipe.extractor.stream.Description
import org.schabi.newpipe.ui.components.common.AppThemeWithSurface
import org.schabi.newpipe.ui.components.common.DescriptionText
import org.schabi.newpipe.ui.components.common.ImagesCloud
import org.schabi.newpipe.ui.components.common.LazyColumnThemedScrollbar
import org.schabi.newpipe.ui.components.common.LoadingIndicator
import org.schabi.newpipe.ui.components.common.MetadataItem
import org.schabi.newpipe.ui.components.common.TagsCloud
import org.schabi.newpipe.util.Localization
import org.schabi.newpipe.util.external_communication.ShareUtils

@Composable
fun ChannelAboutScreen(
    uiState: ChannelUiState,
    onTagClick: (String) -> Unit = {}
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val state = rememberLazyListState()

    when (uiState) {
        is ChannelUiState.Loading -> {
            LoadingIndicator()
        }

        is ChannelUiState.Error -> {
//            ErrorScreen(uiState.message)
        }

        is ChannelUiState.Success -> {
            val details = uiState.channelDetails
            ChannelDetailsContent(details, onTagClick, nestedScrollInterop, state)
        }
    }
}

@Composable
private fun ChannelDetailsContent(
    details: ChannelDetails,
    onTagClick: (String) -> Unit,
    nestedScrollInterop: androidx.compose.ui.input.nestedscroll.NestedScrollConnection,
    state: androidx.compose.foundation.lazy.LazyListState
) {
    val context = LocalContext.current
    LazyColumnThemedScrollbar(state = state) {
        LazyColumn(
            modifier = Modifier
                .nestedScroll(nestedScrollInterop)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state
        ) {
            item {
                DescriptionText(
                    description = details.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (details.subscriberCount != ChannelExtractor.UNKNOWN_SUBSCRIBER_COUNT) {
                item {
                    MetadataItem(
                        label = stringResource(id = R.string.metadata_subscribers),
                        value = Localization.localizeNumber(details.subscriberCount)
                    )
                }
            }
            if (details.avatars.isNotEmpty()) {
                item {
                    ImagesCloud(
                        label = stringResource(id = R.string.metadata_avatars),
                        images = details.avatars,
                        onImageClick = { ShareUtils.openUrlInBrowser(context, it) }
                    )
                }
            }
            if (details.banners.isNotEmpty()) {
                item {
                    ImagesCloud(
                        label = stringResource(id = R.string.metadata_banners),
                        images = details.banners,
                        onImageClick = { ShareUtils.openUrlInBrowser(context, it) }
                    )
                }
            }
            if (details.tags.isNotEmpty()) {
                item {
                    TagsCloud(
                        label = stringResource(id = R.string.metadata_tags).uppercase(),
                        tags = details.tags, onTagClick = onTagClick
                    )
                }
            }
        }
    }
}

@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ChannelAboutScreenPreview() {
    val channelDescription = Description(
        "This is a very long description of a channel that is very interesting. It contains a lot of text to see how it wraps and looks on the screen. It is important to test with a lot of text to make sure everything is working as expected.",
        Description.PLAIN_TEXT
    )
    val subscriberCount = 1234567L
    val tags = listOf(
        "NewPipe", "Android", "Free and Open Source Software (FOSS)", "YouTube", "Music",
        "Streaming", "Privacy", "Ad-free", "Background Playback"
    )
    val uiState = ChannelUiState.Success(
        channelDetails = ChannelDetails(
            description = channelDescription,
            subscriberCount = subscriberCount,
            avatars = listOf(),
            banners = listOf(),
            tags = tags
        )
    )
    AppThemeWithSurface {
        ChannelAboutScreen(
            uiState = uiState
        )
    }
}

@Preview(name = "Light mode - Loading", uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Loading")
@Preview(name = "Dark mode - Loading", uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Loading")
@Composable
fun ChannelAboutScreenLoadingPreview() {
    AppThemeWithSurface {
        ChannelAboutScreen(
            uiState = ChannelUiState.Loading
        )
    }
}

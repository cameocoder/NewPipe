package org.schabi.newpipe.fragments.list.channel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import org.schabi.newpipe.error.ErrorInfo
import org.schabi.newpipe.error.UserAction
import org.schabi.newpipe.extractor.Image
import org.schabi.newpipe.extractor.channel.ChannelInfo
import org.schabi.newpipe.extractor.stream.Description
import org.schabi.newpipe.util.ExtractorHelper

@HiltViewModel(assistedFactory = AboutChannelViewModel.Factory::class)
class AboutChannelViewModel @AssistedInject constructor(
    @Assisted private val serviceId: Int,
    @Assisted private val url: String
) : ViewModel() {

    val channelUiState = MutableStateFlow<ChannelUiState>(ChannelUiState.Loading)

    init {
        subscribeChannelInfo(serviceId, url)
    }

    private var currentWorker: Disposable? = null

    fun subscribeChannelInfo(serviceId: Int, url: String, forceLoad: Boolean = false) {
        currentWorker = ExtractorHelper.getChannelInfo(serviceId, url, forceLoad)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                Consumer { result: ChannelInfo? ->
                    if (result != null) {
                        channelUiState.value =
                            ChannelUiState.Success(result.asChannelDetails())
                    }
                },
                Consumer { throwable: Throwable? ->
                    channelUiState.value = ChannelUiState.Error(
                        ErrorInfo(
                            throwable!!, UserAction.REQUESTED_CHANNEL,
                            url, serviceId, url

                        )
                    )
                }
            )
    }

    @AssistedFactory
    interface Factory {
        fun create(serviceId: Int, url: String): AboutChannelViewModel
    }
}

sealed interface ChannelUiState {
    data object Loading : ChannelUiState
    data class Error(
        val errorInfo: ErrorInfo,
    ) : ChannelUiState

    data class Success(
        val channelDetails: ChannelDetails,
    ) : ChannelUiState
}

data class ChannelDetails(
    val description: Description,
    val subscriberCount: Long,
    val avatars: List<Image> = emptyList(),
    val banners: List<Image> = emptyList(),
    val tags: List<String> = emptyList()
)

fun ChannelInfo.asChannelDetails(): ChannelDetails {
    val description = Description(description, Description.PLAIN_TEXT)
    return ChannelDetails(
        description = description,
        subscriberCount = subscriberCount,
        avatars = avatars,
        banners = banners,
        tags = tags
    )
}

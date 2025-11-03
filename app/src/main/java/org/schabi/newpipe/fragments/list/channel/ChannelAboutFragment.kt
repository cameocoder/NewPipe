package org.schabi.newpipe.fragments.list.channel

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.parcelize.Parcelize
import org.schabi.newpipe.ui.theme.AppTheme
import org.schabi.newpipe.util.NavigationHelper
import org.schabi.newpipe.util.extensions.requireParcelable

@AndroidEntryPoint
class ChannelAboutFragment : Fragment() {

    private val args get() = requireArguments().requireParcelable<Args>(NEW_INSTANCE_ARGS)

    private val viewModel by viewModels<AboutChannelViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<
                AboutChannelViewModel.Factory> { factory ->
                factory.create(args.serviceId, args.url)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        AppTheme {
            Surface {
                val uiState by viewModel.channelUiState.collectAsStateWithLifecycle()
                ChannelAboutScreen(uiState) {
                    onTagClick(it)
                }
            }
        }
    }

    fun getServiceId(): Int {
        return args.serviceId
    }

    private fun onTagClick(tag: String) {
        parentFragment?.let { parentFragment ->
            NavigationHelper.openSearchFragment(
                parentFragment.getParentFragmentManager(),
                getServiceId(), tag
            )
        }
    }

    @Parcelize
    private class Args(
        val serviceId: Int,
        val url: String
    ) : Parcelable

    companion object {
        private const val NEW_INSTANCE_ARGS = "ChannelAboutFragmentArgs"

        fun newInstance(serviceId: Int, url: String) = ChannelAboutFragment().apply {
            arguments = bundleOf(NEW_INSTANCE_ARGS to Args(serviceId, url))
        }
    }
}

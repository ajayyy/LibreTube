package com.github.libretube.util

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.github.libretube.R
import com.github.libretube.constants.IntentData
import com.github.libretube.extensions.toID
import com.github.libretube.ui.activities.MainActivity
import com.github.libretube.ui.fragments.PlayerFragment

object NavigationHelper {
    fun navigateChannel(
        context: Context,
        channelId: String?
    ) {
        if (channelId != null) {
            val activity = context as MainActivity
            val bundle = bundleOf(IntentData.channelId to channelId)
            activity.navController.navigate(R.id.channelFragment, bundle)
            try {
                if (activity.binding.mainMotionLayout.progress == 0.toFloat()) {
                    activity.binding.mainMotionLayout.transitionToEnd()
                    activity.supportFragmentManager.fragments.forEach {
                        (it as PlayerFragment?)?.binding?.playerMotionLayout?.transitionToEnd()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun navigateVideo(
        context: Context,
        videoId: String?,
        playlistId: String? = null
    ) {
        if (videoId != null) {
            val bundle = Bundle()
            bundle.putString(IntentData.videoId, videoId.toID())
            if (playlistId != null) bundle.putString(IntentData.playlistId, playlistId)
            val frag = PlayerFragment()
            frag.arguments = bundle
            val activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .remove(PlayerFragment())
                .commit()
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .commitNow()
        }
    }

    fun navigatePlaylist(
        context: Context,
        playlistId: String?,
        isOwner: Boolean
    ) {
        if (playlistId != null) {
            val activity = context as MainActivity
            val bundle = Bundle()
            bundle.putString(IntentData.playlistId, playlistId)
            bundle.putBoolean("isOwner", isOwner)
            activity.navController.navigate(R.id.playlistFragment, bundle)
        }
    }
}

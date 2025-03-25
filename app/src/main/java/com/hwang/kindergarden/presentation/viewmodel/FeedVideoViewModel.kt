package com.hwang.kindergarden.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedVideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context)
            .build()
    }
    val exoPlayer: ExoPlayer get() = _exoPlayer

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean>
        get() {
            return _isPlaying
        }
    private var mediaUrl: String? = null
    private var playbackState: Int = Player.STATE_IDLE

    init {
        _exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                this@FeedVideoViewModel.playbackState = playbackState
            }
        })
    }

    fun setMedia(url: String) {
        this.mediaUrl = url
        _exoPlayer.setMediaItem(MediaItem.fromUri(url))
        _exoPlayer.prepare()
        _exoPlayer.playWhenReady = false
    }

    fun play() {
        println("h2w, play(): $mediaUrl")
        if (playbackState == Player.STATE_ENDED) {
            _exoPlayer.seekTo(0)
        }
        _exoPlayer.playWhenReady = true
    }

    fun pause() {
        _exoPlayer.playWhenReady = false
    }

    fun togglePlayPause() {
        _exoPlayer.playWhenReady = !_exoPlayer.isPlaying
    }

    override fun onCleared() {
        super.onCleared()
        _exoPlayer.release()
    }
}
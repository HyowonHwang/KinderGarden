package com.hwang.kindergarden.ui.screens.video

import VideoContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hwang.kindergarden.presentation.viewmodel.FeedVideoViewModel

@Composable
fun FeedVideoItem(
    video: VideoContent,
    onClick: () -> Unit,
    shouldPlay: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: FeedVideoViewModel =  hiltViewModel(key = video.id.toString())
) {
    // ExoPlayer 상태 변경 감지
    val isPlaying by viewModel.isPlaying.collectAsState()

    // 미디어 로드 (한 번만 실행)
    LaunchedEffect(key1 = video.contentUrl) {
        viewModel.setMedia(video.contentUrl)
    }

    // shouldPlay 값이 변경될 때마다 재생 상태 업데이트
    LaunchedEffect(shouldPlay) {
        println("h2w, ${video.id}: shouldPlay=$shouldPlay")
        if (shouldPlay) {
            viewModel.play()
        } else {
            viewModel.pause()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 이미지 섹션
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                PlayerSurface(
                    player = viewModel.exoPlayer,
                    surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                    modifier = Modifier
                        .matchParentSize()
                )
                this@Column.AnimatedVisibility(
                    visible = !isPlaying,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    AsyncImage(
                        model = video.image,
                        contentDescription = video.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // 텍스트 정보 섹션
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = video.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
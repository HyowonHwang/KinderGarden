package com.hwang.kindergarden.ui.screens.video

import VideoContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hwang.kindergarden.presentation.viewmodel.VideoContentViewModel
import com.hwang.kindergarden.presentation.viewmodel.VideoContentViewModel.UiState

@Composable
fun VideoContentScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoContentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedVideo by viewModel.selectedVideo.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is UiState.Success -> {
                val videos = (uiState as UiState.Success).videoContents
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(videos.videoContentList) { video ->
                        VideoContentItem(
                            video = video,
                            onClick = { viewModel.getVideoContent(video.id) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                ErrorContent(
                    message = (uiState as UiState.Error).message,
                    onRetry = { viewModel.retryLoading() }
                )
            }
        }

        // 선택된 비디오가 있을 때 표시할 상세 정보
        selectedVideo?.let { video ->
            // 여기에 비디오 상세 정보 또는 플레이어 구현
            VideoDetailDialog(
                video = video,
                onDismiss = { viewModel.clearSelectedVideo() }
            )
        }
    }
}

@Composable
private fun VideoContentItem(
    video: VideoContent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 이미지 섹션
            AsyncImage(
                model = video.image,
                contentDescription = video.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            
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

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun VideoDetailDialog(
    video: VideoContent,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(video.name) },
        text = {
            Column {
                AsyncImage(
                    model = video.image,
                    contentDescription = video.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        modifier = modifier
    )
} 
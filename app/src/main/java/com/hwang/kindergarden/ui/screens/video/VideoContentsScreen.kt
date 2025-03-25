package com.hwang.kindergarden.ui.screens.video

import VideoContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hwang.kindergarden.presentation.viewmodel.VideoContentViewModel
import com.hwang.kindergarden.presentation.viewmodel.VideoContentViewModel.UiState

@Composable
fun VideoContentsScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoContentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedVideo by viewModel.selectedVideo.collectAsState()
    val listState = rememberLazyListState()
    var centerItemIndex by remember { mutableIntStateOf(-1) }
    
    // 스크롤 상태 변화 감지
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                centerItemIndex = if (visibleItems.isNotEmpty()) {
                    visibleItems[visibleItems.size / 2].index
                } else -1
            }
    }

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
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState
                ) {
                    items(videos.videoContentList) { video ->
                        FeedVideoItem(
                            video = video,
                            onClick = { viewModel.getVideoContent(video.id) },
                            shouldPlay = videos.videoContentList.indexOf(video) == centerItemIndex
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
            VideoDetailDialog(
                video = video,
                onDismiss = { viewModel.clearSelectedVideo() }
            )
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
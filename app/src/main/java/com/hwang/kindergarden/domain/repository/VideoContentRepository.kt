package com.hwang.kindergarden.domain.repository

import VideoContent
import VideoContentList

interface VideoContentRepository {
    suspend fun getVideoContents(): Result<VideoContentList>
    suspend fun getVideoContentById(id: Int): Result<VideoContent>
} 

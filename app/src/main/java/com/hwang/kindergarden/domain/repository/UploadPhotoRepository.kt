package com.hwang.kindergarden.domain.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadPhotoRepository {
    suspend fun uploadPhoto(context: Context, file: File): Flow<Result<String>>
}
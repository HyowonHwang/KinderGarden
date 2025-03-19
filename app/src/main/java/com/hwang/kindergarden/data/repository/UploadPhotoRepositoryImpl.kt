package com.hwang.kindergarden.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.hwang.kindergarden.domain.repository.UploadPhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UploadPhotoRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : UploadPhotoRepository {

    override suspend fun uploadPhoto(context: Context, file: File): Flow<Result<String>> =
        callbackFlow {
            withContext(Dispatchers.IO) {
                val responseCallback = object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        trySend(Result.failure(IOException("Unexpected code $e")))
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            trySend(Result.failure(IOException("Unexpected code $response")))
                        }
                        response.body?.string()?.let { body ->
                            JSONObject(body).optString("fullUri").let { fullUri ->
                                if (fullUri.isEmpty()) {
                                    trySend(Result.failure(IOException("Failed to upload photo")))
                                } else {
                                    trySend(Result.success(fullUri))
                                }
                            }
                        } ?: trySend(Result.failure(IOException("Failed to upload photo")))
                    }
                }

                try {
                    val formBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "image",
                            file.name,
                            formBody
                        )
                        .build()

                    val request = Request.Builder()
                        .url("http://192.168.219.108:8000/photos/upload")
                        .post(requestBody)
                        .build()

                    client.newCall(request)
                        .enqueue(responseCallback)
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }
            awaitClose()
        }
}
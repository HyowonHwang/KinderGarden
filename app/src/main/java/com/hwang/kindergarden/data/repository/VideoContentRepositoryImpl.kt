package com.hwang.kindergarden.data.repository

import VideoContent
import VideoContentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hwang.kindergarden.domain.repository.VideoContentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoContentRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : VideoContentRepository {
    
    private val videoContentsRef = database.getReference("videoContentList")

    override suspend fun getVideoContents(): Result<VideoContentList> = try {
        val snapshot = videoContentsRef.get().await()
        val contents = snapshot.children.mapNotNull { 
            it.getValue(VideoContentDto::class.java)?.toDomainModel()
        }
        Result.success(VideoContentList(contents))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getVideoContentById(id: Int): Result<VideoContent> = try {
        val snapshot = videoContentsRef.child(id.toString()).get().await()
        val content = snapshot.getValue(VideoContent::class.java)
            ?: return Result.failure(Exception("Video content not found"))
        Result.success(content)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // 실시간 업데이트를 위한 Flow
    fun getVideoContentsFlow(): Flow<Result<VideoContentList>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val contents = snapshot.children.mapNotNull { 
                        it.getValue(VideoContent::class.java) 
                    }
                    trySend(Result.success(VideoContentList(contents)))
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        
        videoContentsRef.addValueEventListener(listener)
        
        awaitClose {
            videoContentsRef.removeEventListener(listener)
        }
    }
}
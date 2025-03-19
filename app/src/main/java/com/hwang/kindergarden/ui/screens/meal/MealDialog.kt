package com.hwang.kindergarden.ui.screens.meal

import MealData
import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.hwang.kindergarden.presentation.viewmodel.MealDialogViewModel
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (MealData) -> Unit,
    viewModel: MealDialogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }
    var photoFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    
    val context = LocalContext.current
    // 카메라 실행
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoFile?.let { file ->
                viewModel.uploadPhoto(context, file)
            }
        }
    }
    // 카메라 권한 요청
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 승인되면 카메라 실행
            createImageFile(context)?.let { file ->
                photoFile = file  // 🔥 File 저장
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraLauncher.launch(photoUri!!)
            }
        }
    }



    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("식사 정보 입력") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 식사 타입 선택
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                ) {
                    TextField(
                        value = uiState.selectedMealType.toKoreanString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = { Text("식사 종류") }
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        MealType.entries.forEach { mealType ->
                            DropdownMenuItem(
                                text = { Text(mealType.toKoreanString()) },
                                onClick = {
                                    viewModel.updateMealType(mealType)
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }

                // 식사 내용 입력
                TextField(
                    value = uiState.mealContent,
                    onValueChange = { viewModel.updateMealContent(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("식사 내용") },
                    minLines = 3,
                    maxLines = 5
                )

                // 사진 촬영 버튼
                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isUploading
                ) {
                    Text(if (uiState.isUploading) "업로드 중..." else "사진 촬영")
                }

                // 에러 메시지 표시
                uiState.uploadError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(viewModel.getMealData())

                    onDismissRequest()
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        }
    )
}

private fun createImageFile(context: Context): File? {
    return try {
        File.createTempFile(
            "MEAL_${System.currentTimeMillis()}_",
            ".jpg",
            context.cacheDir
        )
    } catch (e: IOException) {
        null
    }
} 
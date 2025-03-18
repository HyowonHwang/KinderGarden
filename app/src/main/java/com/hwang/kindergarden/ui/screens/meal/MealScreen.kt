package com.hwang.kindergarden.ui.screens.meal

import MealItem
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hwang.kindergarden.presentation.viewmodel.MealViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    viewModel: MealViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf(com.hwang.kindergarden.utils.DateUtils.getCurrentDateString()) }
    var showAddMealDialog by remember { mutableStateOf(false) }
    var selectedMealItem by remember { mutableStateOf<MealItem?>(null) }
    
    val dailyMeals by viewModel.dailyMeals.collectAsState()

    // 날짜가 변경될 때마다 데이터 로드
    LaunchedEffect(selectedDate) {
        if (selectedDate != com.hwang.kindergarden.utils.DateUtils.getCurrentDateString()) { // 현재 날짜가 아닐 때만 로드
            viewModel.loadMealsForDate(selectedDate)
        }
        viewModel.setupRealtimeUpdates(selectedDate)
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMealDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 날짜 선택기
            DatePicker(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                }
            )

            // 식단 목록
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(dailyMeals?.meals ?: emptyList()) { meal ->
                    MealItemCard(
                        meal = meal,
                        onEditClick = { selectedMealItem = meal }
                    )
                }
            }
        }

        // 식단 추가/수정 다이얼로그
        if (showAddMealDialog) {
            MealDialog(
                meal = selectedMealItem,
                onDismiss = { 
                    showAddMealDialog = false
                    selectedMealItem = null
                },
                onSave = { meal ->
                    val updatedMeal = meal.copy(
                        date = selectedDate
                    )
                    if (selectedMealItem == null) {
                        viewModel.addMeal(updatedMeal)
                    } else {
                        viewModel.updateMeal(updatedMeal)
                    }
                    showAddMealDialog = false
                    selectedMealItem = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealItemCard(
    meal: MealItem,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onEditClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = meal.type,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = meal.contents,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealDialog(
    meal: MealItem?,
    onDismiss: () -> Unit,
    onSave: (MealItem) -> Unit
) {
    var type by remember { mutableStateOf(meal?.type ?: "") }
    var contents by remember { mutableStateOf(meal?.contents ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (meal == null) "Add Meal" else "Edit Meal") },
        text = {
            Column {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Meal Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = contents,
                    onValueChange = { contents = it },
                    label = { Text("Contents") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        MealItem(
                            id = meal?.id ?: "",
                            type = meal?.type ?: "",
                            contents = contents
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 
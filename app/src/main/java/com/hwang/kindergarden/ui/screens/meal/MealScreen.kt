package com.hwang.kindergarden.ui.screens.meal

import MealData
import MealItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hwang.kindergarden.presentation.viewmodel.MealViewModel
import java.util.UUID
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip


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
                    if (selectedDate != date) {
                        selectedDate = date
                        viewModel.loadMealsForDate(date)
                    }
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

        // 식단 추가 다이얼로그
        if (showAddMealDialog) {
            MealDialog(
                onDismissRequest = {
                    showAddMealDialog = false
                    selectedMealItem = null
                },
                onConfirm = { mealData ->
                    val mealItem = MealItem(
                        id = selectedMealItem?.id ?: UUID.randomUUID().toString(),
                        date = selectedDate,
                        type = mealData.type.value,
                        contents = mealData.content,
                        thumbnailUrl = mealData.thumbnailUrl,
                    )

                    if (selectedMealItem != null) {
                        viewModel.updateMeal(mealItem)
                    } else {
                        viewModel.addMeal(mealItem)
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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = meal.contents,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (meal.thumbnailUrl.isNotEmpty()) {
                    AsyncImage(
                        model = meal.thumbnailUrl,
                        contentDescription = "",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .padding(bottom = 4.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}
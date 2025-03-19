package com.hwang.kindergarden.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp

private var _breadIcon: ImageVector? = null

val BreadIcon: ImageVector
    get() = _breadIcon ?: ImageVector.Builder(
        name = "BreadIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = PathBuilder().apply {
                moveTo(6f, 7f)
                quadTo(6f, 3f, 12f, 2f) // 윗부분 둥글게
                quadTo(18f, 3f, 18f, 7f) // 반대쪽 둥글게
                lineTo(18f, 19f) // 아래로 직선
                arcTo(1.5f, 1.5f, 0f, false, true, 16.5f, 20.5f) // 둥근 모서리
                lineTo(7.5f, 20.5f) // 반대쪽 직선
                arcTo(1.5f, 1.5f, 0f, false, true, 6f, 19f) // 둥근 모서리
                close()
            }.nodes,
            fill = SolidColor(Color(0xFFFFD28D)), // 연한 갈색
            fillAlpha = 1.0f,
            stroke = SolidColor(Color(0xFF8B5A2B)), // 테두리 진한 갈색
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.5f,
            pathFillType = PathFillType.NonZero
        )
    }.build()
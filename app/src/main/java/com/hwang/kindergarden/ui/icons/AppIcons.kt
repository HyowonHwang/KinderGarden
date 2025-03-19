import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.unit.dp

val Icons.Filled.Chick: ImageVector
    get() {
        if (_chick != null) {
            return _chick!!
        }
        _chick = Builder(
            name = "Filled.Chick",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).materialPath {
            // 몸통
            moveTo(12.0f, 16.0f)
            arcToRelative(4.0f, 4.0f, 0.0f, true, false, 0.0f, -8.0f)
            arcToRelative(4.0f, 4.0f, 0.0f, false, false, 0.0f, 8.0f)

            // 머리
            moveTo(8.0f, 8.0f)
            arcToRelative(2.5f, 2.5f, 0.0f, true, false, 0.0f, -5.0f)
            arcToRelative(2.5f, 2.5f, 0.0f, false, false, 0.0f, 5.0f)

            // 부리
            moveTo(10.5f, 6.5f)
            lineTo(11.5f, 6.5f)
            lineTo(11.0f, 7.5f)
            close()

            // 눈
            moveTo(7.5f, 5.5f)
            arcToRelative(0.5f, 0.5f, 0.0f, true, false, 0.0f, -1.0f)
            arcToRelative(0.5f, 0.5f, 0.0f, false, false, 0.0f, 1.0f)
        }.build()
        return _chick!!
    }

private var _chick: ImageVector? = null
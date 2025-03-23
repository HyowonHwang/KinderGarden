import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hwang.kindergarden.presentation.viewmodel.MainViewModel


@Composable
fun ShowMain(
    modifier: Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val shouldShowWelcome by viewModel.shouldShowWelcome.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            if (shouldShowWelcome) {
                Welcome(
                    modifier = modifier,
                    onWelcomeComplete = {
                        viewModel.setShouldShowWelcome(false)
                    }
                )
            } else {
                MainScreen(modifier = modifier)
            }
        }
    }

}


@Composable
fun Welcome(modifier: Modifier, onWelcomeComplete: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome!! KinderGarden",
            style = TextStyle(fontSize = 20.sp),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onWelcomeComplete
        ) {
            Text("Continue")
        }
    }
}

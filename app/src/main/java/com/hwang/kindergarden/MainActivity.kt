package com.hwang.kindergarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hwang.kindergarden.ui.theme.KinderGardenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KinderGardenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShowMain(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun ShowMain(modifier: Modifier) {
    var shouldWelcomePage by rememberSaveable { mutableStateOf(false) }

    if(!shouldWelcomePage) {
        Welcome(modifier, onClick = {
            shouldWelcomePage = true
        })
    } else {
        MainItems(modifier)
    }
}

@Composable
fun Welcome(modifier: Modifier, onClick: () -> Unit = {}) {
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
            onClick = onClick
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun MainItems(modifier: Modifier) {

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KinderGardenTheme {
        ShowMain(Modifier)
    }
}
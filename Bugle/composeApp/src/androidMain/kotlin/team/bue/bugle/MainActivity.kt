package team.bue.bugle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.ui.BugleApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            BugleTheme {
                BugleApp()
            }
        }
    }
}

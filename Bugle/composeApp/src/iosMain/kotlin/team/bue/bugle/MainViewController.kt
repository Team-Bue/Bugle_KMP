package team.bue.bugle

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import team.bue.bugle.ui.BugleApp

@Suppress("FunctionName")
fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { BugleApp() }
}

package team.bue.bugle.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBarType
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBarVisuals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberBugleAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
): BugleAppState {
    return remember(
        coroutineScope,
        snackBarHostState,
    ) {
        BugleAppState(
            coroutineScope = coroutineScope,
            snackBarHostState = snackBarHostState,
        )
    }
}

@Stable
class BugleAppState(
    val coroutineScope: CoroutineScope,
    val snackBarHostState: SnackbarHostState,
) {
    private fun showSnackBar(
        visuals: BugleSnackBarVisuals,
        duration: Duration = 2.seconds,
    ) {
        coroutineScope.launch {
            withTimeoutOrNull(duration) {
                snackBarHostState.showSnackbar(visuals)
            }
        }
    }

    fun showSnackBar(
        snackBarType: BugleSnackBarType,
        message: String,
    ) {
        showSnackBar(
            visuals = BugleSnackBarVisuals(
                snackBarType = snackBarType,
                message = message,
            ),
        )
    }
}
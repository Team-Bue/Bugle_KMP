package team.bue.bugle.core.datastore.storage

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.context.GlobalContext
import team.bue.bugle.core.model.auth.TokenPair

private const val PREFERENCES_NAME = "bugle_auth_preferences"
private const val ACCESS_TOKEN_KEY = "access_token"
private const val REFRESH_TOKEN_KEY = "refresh_token"

private class AndroidJwtTokenStore(
    private val sharedPreferences: SharedPreferences,
) : JwtTokenStore {
    override suspend fun save(tokenPair: TokenPair) {
        sharedPreferences
            .edit()
            .putString(ACCESS_TOKEN_KEY, tokenPair.accessToken)
            .putString(REFRESH_TOKEN_KEY, tokenPair.refreshToken)
            .apply()
    }

    override suspend fun get(): TokenPair? {
        val accessToken = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
        return if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            null
        } else {
            TokenPair(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

    override suspend fun clear() {
        sharedPreferences
            .edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()
    }
}

actual fun createJwtTokenStore(): JwtTokenStore {
    val context: Context = GlobalContext.get().get()
    val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    return AndroidJwtTokenStore(sharedPreferences)
}

package team.bue.bugle.core.datastore.storage

import team.bue.bugle.core.model.auth.TokenPair
import java.util.prefs.Preferences

private const val ACCESS_TOKEN_KEY = "access_token"
private const val REFRESH_TOKEN_KEY = "refresh_token"

private class JvmJwtTokenStore : JwtTokenStore {
    private val preferences = Preferences.userRoot().node("team/bue/bugle/auth")

    override suspend fun save(tokenPair: TokenPair) {
        preferences.put(ACCESS_TOKEN_KEY, tokenPair.accessToken)
        preferences.put(REFRESH_TOKEN_KEY, tokenPair.refreshToken)
    }

    override suspend fun get(): TokenPair? {
        val accessToken = preferences.get(ACCESS_TOKEN_KEY, null)
        val refreshToken = preferences.get(REFRESH_TOKEN_KEY, null)
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
        preferences.remove(ACCESS_TOKEN_KEY)
        preferences.remove(REFRESH_TOKEN_KEY)
    }
}

actual fun createJwtTokenStore(): JwtTokenStore = JvmJwtTokenStore()

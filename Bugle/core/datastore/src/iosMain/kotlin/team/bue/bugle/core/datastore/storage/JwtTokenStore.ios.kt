package team.bue.bugle.core.datastore.storage

import platform.Foundation.NSUserDefaults
import team.bue.bugle.core.model.auth.TokenPair

private const val ACCESS_TOKEN_KEY = "access_token"
private const val REFRESH_TOKEN_KEY = "refresh_token"

private class IosJwtTokenStore(
    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults,
) : JwtTokenStore {
    override suspend fun save(tokenPair: TokenPair) {
        userDefaults.setObject(tokenPair.accessToken, ACCESS_TOKEN_KEY)
        userDefaults.setObject(tokenPair.refreshToken, REFRESH_TOKEN_KEY)
    }

    override suspend fun get(): TokenPair? {
        val accessToken = userDefaults.stringForKey(ACCESS_TOKEN_KEY)
        val refreshToken = userDefaults.stringForKey(REFRESH_TOKEN_KEY)
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
        userDefaults.removeObjectForKey(ACCESS_TOKEN_KEY)
        userDefaults.removeObjectForKey(REFRESH_TOKEN_KEY)
    }
}

actual fun createJwtTokenStore(): JwtTokenStore = IosJwtTokenStore()

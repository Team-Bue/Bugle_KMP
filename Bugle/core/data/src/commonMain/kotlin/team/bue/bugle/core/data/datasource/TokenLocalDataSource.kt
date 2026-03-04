package team.bue.bugle.core.data.datasource

import team.bue.bugle.core.datastore.storage.JwtTokenStore
import team.bue.bugle.core.model.auth.TokenPair

interface TokenLocalDataSource {
    suspend fun save(tokenPair: TokenPair)

    suspend fun get(): TokenPair?

    suspend fun clear()
}

class TokenLocalDataSourceImpl(
    private val jwtTokenStore: JwtTokenStore,
) : TokenLocalDataSource {
    override suspend fun save(tokenPair: TokenPair) {
        jwtTokenStore.save(tokenPair)
    }

    override suspend fun get(): TokenPair? = jwtTokenStore.get()

    override suspend fun clear() {
        jwtTokenStore.clear()
    }
}

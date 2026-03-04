package team.bue.bugle.core.datastore.storage

import team.bue.bugle.core.model.auth.TokenPair

interface JwtTokenStore {
    suspend fun save(tokenPair: TokenPair)

    suspend fun get(): TokenPair?

    suspend fun clear()
}

expect fun createJwtTokenStore(): JwtTokenStore

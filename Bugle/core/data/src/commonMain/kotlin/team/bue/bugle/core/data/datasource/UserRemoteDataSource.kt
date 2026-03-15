package team.bue.bugle.core.data.datasource

import team.bue.bugle.core.model.user.MyProfile
import team.bue.bugle.core.network.dto.user.toDomain
import team.bue.bugle.core.network.service.UserApiService

interface UserRemoteDataSource {
    suspend fun getMyProfile(accessToken: String): MyProfile
}

class UserRemoteDataSourceImpl(
    private val userApiService: UserApiService,
) : UserRemoteDataSource {
    override suspend fun getMyProfile(accessToken: String): MyProfile {
        return userApiService.getMyProfile(accessToken).toDomain()
    }
}

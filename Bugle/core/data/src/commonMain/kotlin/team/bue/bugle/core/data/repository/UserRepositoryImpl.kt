package team.bue.bugle.core.data.repository

import team.bue.bugle.core.data.datasource.UserRemoteDataSource
import team.bue.bugle.core.domain.repository.UserRepository
import team.bue.bugle.core.model.user.MyProfile

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {
    override suspend fun getMyProfile(accessToken: String): Result<MyProfile> =
        runCatching {
            userRemoteDataSource.getMyProfile(accessToken)
        }
}

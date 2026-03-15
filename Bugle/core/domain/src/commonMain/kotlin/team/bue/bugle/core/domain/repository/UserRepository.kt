package team.bue.bugle.core.domain.repository

import team.bue.bugle.core.model.user.MyProfile

interface UserRepository {
    suspend fun getMyProfile(accessToken: String): Result<MyProfile>
}

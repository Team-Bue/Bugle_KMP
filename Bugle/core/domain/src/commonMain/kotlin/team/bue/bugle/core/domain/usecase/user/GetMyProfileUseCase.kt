package team.bue.bugle.core.domain.usecase.user

import team.bue.bugle.core.domain.repository.UserRepository
import team.bue.bugle.core.model.user.MyProfile

class GetMyProfileUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(accessToken: String): Result<MyProfile> {
        return userRepository.getMyProfile(accessToken)
    }
}

package team.bue.bugle.core.data.di

import org.koin.dsl.module
import team.bue.bugle.core.data.datasource.AuthRemoteDataSource
import team.bue.bugle.core.data.datasource.AuthRemoteDataSourceImpl
import team.bue.bugle.core.data.datasource.MailRemoteDataSource
import team.bue.bugle.core.data.datasource.MailRemoteDataSourceImpl
import team.bue.bugle.core.data.datasource.TokenLocalDataSource
import team.bue.bugle.core.data.datasource.TokenLocalDataSourceImpl
import team.bue.bugle.core.data.datasource.UserRemoteDataSource
import team.bue.bugle.core.data.datasource.UserRemoteDataSourceImpl
import team.bue.bugle.core.data.repository.AuthRepositoryImpl
import team.bue.bugle.core.data.repository.MailRepositoryImpl
import team.bue.bugle.core.data.repository.UserRepositoryImpl
import team.bue.bugle.core.datastore.storage.JwtTokenStore
import team.bue.bugle.core.datastore.storage.createJwtTokenStore
import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.domain.repository.MailRepository
import team.bue.bugle.core.domain.repository.UserRepository
import team.bue.bugle.core.domain.usecase.auth.FindAccountIdUseCase
import team.bue.bugle.core.domain.usecase.auth.LoginUseCase
import team.bue.bugle.core.domain.usecase.auth.ResetPasswordUseCase
import team.bue.bugle.core.domain.usecase.auth.SignUpUseCase
import team.bue.bugle.core.domain.usecase.mail.SendMailCodeUseCase
import team.bue.bugle.core.domain.usecase.mail.VerifyMailCodeUseCase
import team.bue.bugle.core.domain.usecase.user.GetMyProfileUseCase
import team.bue.bugle.core.network.config.BugleNetworkConfig
import team.bue.bugle.core.network.http.createBugleHttpClient
import team.bue.bugle.core.network.service.AuthApiService
import team.bue.bugle.core.network.service.CommentApiService
import team.bue.bugle.core.network.service.FileApiService
import team.bue.bugle.core.network.service.FollowApiService
import team.bue.bugle.core.network.service.LikeApiService
import team.bue.bugle.core.network.service.MailApiService
import team.bue.bugle.core.network.service.PostApiService
import team.bue.bugle.core.network.service.ReportApiService
import team.bue.bugle.core.network.service.SearchApiService
import team.bue.bugle.core.network.service.UserApiService

private const val DEFAULT_BUGLE_BASE_URL = "https://api.bugle.site"

val coreApiModule =
    module {
        single {
            BugleNetworkConfig(
                baseUrl = getProperty("BUGLE_BASE_URL", DEFAULT_BUGLE_BASE_URL),
            )
        }
        single { createBugleHttpClient(get()) }

        single { AuthApiService(get()) }
        single { MailApiService(get()) }
        single { UserApiService(get()) }
        single { PostApiService(get()) }
        single { CommentApiService(get()) }
        single { FileApiService(get()) }
        single { SearchApiService(get()) }
        single { FollowApiService(get()) }
        single { LikeApiService(get()) }
        single { ReportApiService(get()) }

        single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
        single<MailRemoteDataSource> { MailRemoteDataSourceImpl(get()) }
        single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
        single<JwtTokenStore> { createJwtTokenStore() }
        single<TokenLocalDataSource> { TokenLocalDataSourceImpl(get()) }

        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        single<MailRepository> { MailRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }

        factory { LoginUseCase(get()) }
        factory { SignUpUseCase(get()) }
        factory { FindAccountIdUseCase(get()) }
        factory { ResetPasswordUseCase(get()) }
        factory { SendMailCodeUseCase(get()) }
        factory { VerifyMailCodeUseCase(get()) }
        factory { GetMyProfileUseCase(get()) }
    }

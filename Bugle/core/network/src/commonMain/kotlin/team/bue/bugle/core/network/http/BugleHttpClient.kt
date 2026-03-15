package team.bue.bugle.core.network.http

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import team.bue.bugle.core.network.config.BugleNetworkConfig
import io.ktor.client.plugins.logging.Logger as KtorLogger

fun createBugleHttpClient(config: BugleNetworkConfig): HttpClient =
    HttpClient(platformHttpClientEngineFactory()) {
        defaultRequest {
            url(config.baseUrl)
            accept(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                },
            )
        }
        install(Logging) {
            level = LogLevel.BODY
            logger =
                object : KtorLogger {
                    override fun log(message: String) {
                        Logger.withTag("BugleHttpClient").i { message }
                    }
                }
        }
    }

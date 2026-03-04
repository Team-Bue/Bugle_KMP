package team.bue.bugle.core.network.http

import io.ktor.client.engine.HttpClientEngineFactory

internal expect fun platformHttpClientEngineFactory(): HttpClientEngineFactory<*>

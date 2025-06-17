package com.jesushz.notemarkmilestone.core.di

import com.jesushz.notemarkmilestone.core.data.auth.EncryptedSessionStorage
import com.jesushz.notemarkmilestone.core.data.networking.HttpClientFactory
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single {
        CIO.create()
    }
    single {
        HttpClientFactory(get(), get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}
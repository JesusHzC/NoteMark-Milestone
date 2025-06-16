package com.jesushz.notemarkmilestone.auth.di

import com.jesushz.notemarkmilestone.auth.data.EmailPatternValidator
import com.jesushz.notemarkmilestone.auth.domain.PatternValidator
import com.jesushz.notemarkmilestone.auth.domain.UserDataValidator
import com.jesushz.notemarkmilestone.auth.presentation.login.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)

    viewModelOf(::LoginViewModel)
}
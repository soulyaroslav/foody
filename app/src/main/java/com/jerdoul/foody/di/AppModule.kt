package com.jerdoul.foody.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.jerdoul.foody.data.error.NetworkErrorHandlerImpl
import com.jerdoul.foody.data.error.NetworkErrorParserImpl
import com.jerdoul.foody.presentation.navigation.DefaultNavigator
import com.jerdoul.foody.presentation.navigation.Navigator
import com.jerdoul.foody.data.network.NetworkManagerImpl
import com.jerdoul.foody.data.repository.NetworkRepositoryImpl
import com.jerdoul.foody.data.validation.EmailValidationProcessorImpl
import com.jerdoul.foody.data.validation.ValidationPatternProviderImpl
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.NetworkErrorParser
import com.jerdoul.foody.domain.network.NetworkManager
import com.jerdoul.foody.domain.repository.NetworkRepository
import com.jerdoul.foody.domain.validation.ValidationPatternProvider
import com.jerdoul.foody.domain.validation.ValidationProcessor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun providesValidationProcessors(patternProvider: ValidationPatternProvider): ValidationProcessor {
        return ValidationProcessor.initializeChain(
            EmailValidationProcessorImpl(patternProvider = patternProvider)
        )
    }

    @Provides
    @Singleton
    @GsonParserQualifier
    fun provideGson(): Gson = Gson()

}

@Module
@InstallIn(SingletonComponent::class)
interface BindingsModule {
    @Binds
    @Singleton
    fun bindNavigator(navigator: DefaultNavigator): Navigator

    @Binds
    @Singleton
    fun bindNetworkManager(navigator: NetworkManagerImpl): NetworkManager

    @Binds
    @Singleton
    fun bindValidationPatternProvider(provider: ValidationPatternProviderImpl): ValidationPatternProvider

    @Binds
    @Singleton
    fun bindNetworkRepository(repository: NetworkRepositoryImpl): NetworkRepository

    @Binds
    @Singleton
    fun bindNetworkErrorHandler(handler: NetworkErrorHandlerImpl): NetworkErrorHandler

    @Binds
    @Singleton
    fun bindNetworkErrorParser(parser: NetworkErrorParserImpl): NetworkErrorParser
}
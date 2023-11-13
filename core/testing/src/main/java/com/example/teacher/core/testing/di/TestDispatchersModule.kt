package com.example.teacher.core.testing.di

import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.common.di.DispatchersModule
import com.example.teacher.core.common.di.IoDispatcher
import com.example.teacher.core.common.di.MainDispatcher
import com.example.teacher.core.common.di.MainImmediateDispatcher
import com.example.teacher.core.common.di.UnconfinedDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
object TestDispatchersModule {

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @Provides
    @MainImmediateDispatcher
    fun providesMainImmediateDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher

    @UnconfinedDispatcher
    @Provides
    fun providesUnconfinedDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher
}
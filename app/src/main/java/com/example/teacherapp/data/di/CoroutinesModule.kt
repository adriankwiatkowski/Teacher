package com.example.teacherapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val immediate: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}

//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class DefaultDispatcher
//
//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class IoDispatcher
//
//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class MainDispatcher
//
//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class MainImmediateDispatcher
//
//@DefaultDispatcher
//@Provides
//fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
//
//@IoDispatcher
//@Provides
//fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
//
//@MainDispatcher
//@Provides
//fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
//
//@MainImmediateDispatcher
//@Provides
//fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
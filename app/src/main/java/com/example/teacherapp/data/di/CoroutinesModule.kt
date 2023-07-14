package com.example.teacherapp.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainImmediateDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnconfinedDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @MainImmediateDispatcher
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @UnconfinedDispatcher
    @Provides
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

//    @Provides
//    @Singleton
//    fun provideDispatcherProvider(): DispatcherProvider {
//        return DefaultDispatcherProvider()
//    }
}

//interface DispatcherProvider {
//    val main: CoroutineDispatcher
//    val immediate: CoroutineDispatcher
//    val io: CoroutineDispatcher
//    val default: CoroutineDispatcher
//    val unconfined: CoroutineDispatcher
//}
//
//class DefaultDispatcherProvider : DispatcherProvider {
//    override val main: CoroutineDispatcher
//        get() = Dispatchers.Main
//    override val immediate: CoroutineDispatcher
//        get() = Dispatchers.Main.immediate
//    override val io: CoroutineDispatcher
//        get() = Dispatchers.IO
//    override val default: CoroutineDispatcher
//        get() = Dispatchers.Default
//    override val unconfined: CoroutineDispatcher
//        get() = Dispatchers.Unconfined
//}
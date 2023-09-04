package com.example.teacher.core.auth.di

import com.example.teacher.core.auth.Auth
import com.example.teacher.core.auth.AuthImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {
    @Binds
    fun bindsAuth(auth: AuthImpl): Auth
}
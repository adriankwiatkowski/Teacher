package com.example.teacherapp.core.auth.di

import com.example.teacherapp.core.auth.Auth
import com.example.teacherapp.core.auth.AuthImpl
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
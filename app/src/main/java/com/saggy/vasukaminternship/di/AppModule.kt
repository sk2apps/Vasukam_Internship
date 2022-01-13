package com.saggy.vasukaminternship.di

import com.saggy.vasukaminternship.domain.repository.FirebaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    @Singleton
//    fun provideApplication(): Application{
//        return Application()
//    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository{
        return FirebaseRepository()
    }


}
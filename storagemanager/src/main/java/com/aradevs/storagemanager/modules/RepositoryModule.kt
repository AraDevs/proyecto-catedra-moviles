package com.aradevs.storagemanager.modules

import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource
import com.aradevs.storagemanager.repositories.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun providesDatabaseRepository(localDataSource: DatabaseLocalDataSource): DatabaseRepository {
        return DatabaseRepository(localDataSource)
    }
}
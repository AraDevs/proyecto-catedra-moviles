package com.aradevs.storagemanager.modules

import com.aradevs.storagemanager.repositories.DatabaseRepository
import com.aradevs.storagemanager.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {
    @Provides
    fun providesSaveMedicineUseCase(repository: DatabaseRepository): SaveMedicineUseCase =
        SaveMedicineUseCase(repository)

    @Provides
    fun providesGetMedicinesUseCase(repository: DatabaseRepository): GetMedicinesUseCase =
        GetMedicinesUseCase(repository)

    @Provides
    fun providesGetAllMedicineUseCase(repository: DatabaseRepository): GetAllMedicinesUseCase =
        GetAllMedicinesUseCase(repository)

    @Provides
    fun providesUpdateMedicineUseCase(repository: DatabaseRepository): UpdateMedicineUseCase =
        UpdateMedicineUseCase(repository)

    @Provides
    fun providesDeleteMedicineUseCase(repository: DatabaseRepository): DeleteMedicineUseCase =
        DeleteMedicineUseCase(repository)
}
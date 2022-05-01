package com.aradevs.storagemanager.use_cases

import com.aradevs.domain.Medicine
import com.aradevs.storagemanager.repositories.DatabaseRepository

class SaveMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicine: Medicine) =
        repository.saveMedicine(medicine)
}

class GetMedicinesUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getMedicines()
}

class GetAllMedicinesUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getAllMedicines()
}

class DeactivateMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicine: Medicine) = repository.deactivateMedicine(medicine)
}

class DeleteMedicineUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(medicineId: Int) = repository.deleteMedicine(medicineId)
}




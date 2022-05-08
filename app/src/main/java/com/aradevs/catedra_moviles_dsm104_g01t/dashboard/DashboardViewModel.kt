package com.aradevs.catedra_moviles_dsm104_g01t.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.DeactivateMedicineUseCase
import com.aradevs.storagemanager.use_cases.GetMedicinesUseCase
import com.aradevs.storagemanager.use_cases.SaveMedicineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getMedicinesUseCase: GetMedicinesUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val deactivateMedicineUseCase: DeactivateMedicineUseCase,
) :
    ViewModel() {

    private val _medicinesStatus: MutableLiveData<Status<List<Medicine>>> =
        MutableLiveData(Status.Loading())
    val medicineStatus: LiveData<Status<List<Medicine>>> get() = _medicinesStatus

    val medicineList: MutableList<Medicine> = mutableListOf()

    /**
     * Obtains the list of [Medicine] from the database
     */
    fun getMedicines() {
        _medicinesStatus.postValue(Status.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getMedicinesUseCase()) {
                is Status.Success -> {
                    medicineList.clear()
                    medicineList.addAll(status.data)
                    _medicinesStatus.postValue(status)
                }
                is Status.Error -> _medicinesStatus.postValue(status)
                else -> _medicinesStatus.postValue(Status.Loading())
            }
        }
    }

    /**
     * Sets a medicine as inactive
     */
    fun setMedicineAsInactive(medicine: Medicine, onNotificationSetup: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (deactivateMedicineUseCase(medicine.copy(status = false))) {
                is Status.Success -> {
                    getMedicines()
                    onNotificationSetup()
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    /**
     * Saves a medicine in the database
     */
    fun saveMedicine(medicine: Medicine, onNotificationSetup: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (saveMedicineUseCase(medicine)) {
                is Status.Success -> {
                    getMedicines()
                    onNotificationSetup()
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

}
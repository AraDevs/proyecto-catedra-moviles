package com.aradevs.catedra_moviles_dsm104_g01t.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.GetMedicinesUseCase
import com.aradevs.storagemanager.use_cases.SaveMedicineUseCase
import com.aradevs.storagemanager.use_cases.UpdateMedicineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getMedicinesUseCase: GetMedicinesUseCase,
    private val saveMedicineUseCase: SaveMedicineUseCase,
    private val updateMedicineUseCase: UpdateMedicineUseCase,
) :
    ViewModel() {

    private val _medicinesStatus: MutableLiveData<Status<List<Medicine>>> =
        MutableLiveData(Status.Loading())
    val medicineStatus: LiveData<Status<List<Medicine>>> get() = _medicinesStatus

    val medicineList: MutableList<Medicine> = mutableListOf()

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

    fun setMedicineAsInactive(medicine: Medicine) {
        viewModelScope.launch(Dispatchers.IO) {
            when (updateMedicineUseCase(medicine.copy(status = false))) {
                is Status.Success -> getMedicines()
                else -> {
                    //do nothing
                }
            }
        }
    }

    fun saveMedicine(medicine: Medicine) {
        viewModelScope.launch(Dispatchers.IO) {
            when (saveMedicineUseCase(medicine)) {
                is Status.Success -> getMedicines()
                else -> {
                    //do nothing
                }
            }
        }
    }

}
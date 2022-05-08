package com.aradevs.catedra_moviles_dsm104_g01t.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.DeactivateMedicineUseCase
import com.aradevs.storagemanager.use_cases.GetAllMedicinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase,
    private val deactivateMedicineUseCase: DeactivateMedicineUseCase,
) :
    ViewModel() {

    private val _medicinesStatus: MutableLiveData<Status<List<Medicine>>> =
        MutableLiveData(Status.Loading())
    val medicineStatus: LiveData<Status<List<Medicine>>> get() = _medicinesStatus
    val medicineList: MutableList<Medicine> = mutableListOf()
    var providedDate: Date = Date()

    /**
     * Requests the list of medicines (active or inactive) stored in the database
     */
    fun getMedicines() {
        _medicinesStatus.postValue(Status.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getAllMedicinesUseCase()) {
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
}
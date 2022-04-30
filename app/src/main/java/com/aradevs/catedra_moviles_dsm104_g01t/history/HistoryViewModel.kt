package com.aradevs.catedra_moviles_dsm104_g01t.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.DeleteMedicineUseCase
import com.aradevs.storagemanager.use_cases.GetAllMedicinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
) : ViewModel() {
    private val _medicinesStatus: MutableLiveData<Status<List<Medicine>>> =
        MutableLiveData(Status.Loading())
    val medicineStatus: LiveData<Status<List<Medicine>>> get() = _medicinesStatus

    fun getMedicines() {
        _medicinesStatus.postValue(Status.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getAllMedicinesUseCase()) {
                is Status.Success -> {
                    _medicinesStatus.postValue(status)
                }
                is Status.Error -> _medicinesStatus.postValue(status)
                else -> _medicinesStatus.postValue(Status.Loading())
            }
        }
    }

    fun deleteMedicine(medicineId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = deleteMedicineUseCase(medicineId)) {
                is Status.Success -> getMedicines()
                else -> {
                    //do nothing
                }
            }
        }
    }

}
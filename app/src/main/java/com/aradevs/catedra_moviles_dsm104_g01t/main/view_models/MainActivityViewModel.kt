package com.aradevs.catedra_moviles_dsm104_g01t.main.view_models

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.catedra_moviles_dsm104_g01t.requireFutureDates
import com.aradevs.catedra_moviles_dsm104_g01t.scheduler.NotificationScheduler
import com.aradevs.catedra_moviles_dsm104_g01t.setAsTodayDate
import com.aradevs.domain.Medicine
import com.aradevs.domain.Notification
import com.aradevs.domain.SpanType
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.DeleteNotificationsUseCase
import com.aradevs.storagemanager.use_cases.GetMedicinesUseCase
import com.aradevs.storagemanager.use_cases.GetNotificationsUseCase
import com.aradevs.storagemanager.use_cases.SaveNotificationUseCase
import com.c3rberuss.androidutils.toDayMonthYearHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getMedicinesUseCase: GetMedicinesUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val deleteNotificationsUseCase: DeleteNotificationsUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
    private val scheduler: NotificationScheduler,
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null

    /**
     * Obtains a list of active [Medicine] and filters the ones that need a notification
     * then, calls [deleteNotificationsAndSetupNewOnes]
     */
    private fun getMedicinesForNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getMedicinesUseCase()) {
                is Status.Success -> {
                    Timber.d("medicines success")
                    val filteredList =
                        status.data.filter { medicine -> medicine.requiresNotification }
                    val medicineTakesList: MutableList<Medicine> = mutableListOf()
                    filteredList.forEach { medicine ->
                        medicine.startDate.setAsTodayDate()
                            .requireFutureDates(medicine.repeatInterval.toInt(), SpanType.WEEK)
                            .forEach {
                                medicineTakesList.add(medicine.copy(startDate = it))
                            }
                    }
                    deleteNotificationsAndSetupNewOnes(medicineTakesList)
                }
                else -> {
                    Timber.d(status.toString())
                }
            }
        }
    }

    /**
     * Obtains the list of [Notification] stored in the database, then cancels all the
     * alarm managers related to each notification, finally calls [getMedicinesForNotifications]
     */
    fun cancelNotificationsAlarmManagersAndSetNewOnes() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getNotificationsUseCase()) {
                is Status.Success -> {
                    context?.let {
                        scheduler.cancelNotifications(it, status.data)
                        getMedicinesForNotifications()
                    }
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    /**
     * Deletes all [Notification] in the database
     */
    private fun deleteNotificationsAndSetupNewOnes(items: List<Medicine>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (deleteNotificationsUseCase()) {
                is Status.Success -> {
                    Timber.d("Delete notifications success")
                    setupNotifications(items)
                }
                else -> {
                    Timber.e("Failed")
                }
            }
        }
    }

    /**
     * Sets up new notifications
     */
    private fun setupNotifications(items: List<Medicine>) {
        Timber.d("item count ${items.size}")
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach {
                when (val status = saveNotificationUseCase(Notification(0,
                    "${it.name} - ${it.startDate.toDayMonthYearHour()}",
                    it.startDate))) {
                    is Status.Success -> Timber.d("Saved  success")
                    is Status.Error -> Timber.d(status.exception)
                    else -> {
                        //do nothing
                    }
                }
            }
            setupNotificationSchedulers()
        }
    }

    /**
     * Sets up alarm managers for each notification
     */
    private fun setupNotificationSchedulers() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val status = getNotificationsUseCase()) {
                is Status.Success -> {
                    context?.let {
                        status.data.forEach { notification ->
                            scheduler.setupNotification(it,
                                notification.id,
                                notification.date,
                                60)
                        }
                    }
                }
                else -> {
                    //do nothing
                }
            }
        }
    }
}
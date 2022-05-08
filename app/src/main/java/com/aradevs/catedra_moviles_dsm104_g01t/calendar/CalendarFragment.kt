package com.aradevs.catedra_moviles_dsm104_g01t.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.adapters.MedicineListAdapter
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.FragmentCalendarBinding
import com.aradevs.catedra_moviles_dsm104_g01t.requireFutureDates
import com.aradevs.catedra_moviles_dsm104_g01t.setAsProvidedDate
import com.aradevs.catedra_moviles_dsm104_g01t.setAsTodayDate
import com.aradevs.domain.Medicine
import com.aradevs.domain.RenderLocation
import com.aradevs.domain.SpanType
import com.aradevs.domain.coroutines.Status
import com.c3rberuss.androidutils.dayOfMonth
import com.c3rberuss.androidutils.showThisAndHide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private val binding: FragmentCalendarBinding by viewBinding()
    private val viewModel: CalendarViewModel by viewModels()

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMedicineStatus()
        viewModel.getMedicines()
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            viewModel.providedDate =
                SimpleDateFormat("yyyy-MM-dd").parse("$year-${month + 1}-$day") as Date
            setupUI(getDateMedicines(viewModel.medicineList))
        }

    }

    /**
     * Observes the medicine request status
     */
    private fun observeMedicineStatus() {
        viewModel.medicineStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    Snackbar.make(binding.root,
                        getString(R.string.error_obtaining_info),
                        BaseTransientBottomBar.LENGTH_SHORT).show()
                }
                is Status.Success -> {
                    setupUI(getDateMedicines(viewModel.medicineList))
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    /**
     * Set's up the list container and validates if the provided list is null or empty
     */
    private fun setupUI(items: List<Medicine>) {
        if (items.isNullOrEmpty()) {
            binding.emptyMedicines.root.showThisAndHide(binding.medicineList)
            return
        }
        binding.medicineList.showThisAndHide(binding.emptyMedicines.root)
        binding.medicineList.adapter = MedicineListAdapter(true, RenderLocation.CALENDAR) {
            onMedicineDeleteTapped(it)
        }.apply {
            submitList(items)
        }
    }

    /**
     * Triggers actions when the "delete" item is tapped
     */
    private fun onMedicineDeleteTapped(medicine: Medicine) {
        try {
            val listAdapter = binding.medicineList.adapter as MedicineListAdapter
            val editableList = listAdapter.currentList.toMutableList()
            editableList.removeIf { listMedicine ->
                medicine.id == listMedicine.id
            }
            listAdapter.submitList(editableList)
            if (editableList.isNullOrEmpty()) {
                binding.emptyMedicines.root.showThisAndHide(binding.medicineList)
            }
        } catch (e: Exception) {
            Timber.d(e.message)
        }
    }

    /**
     * Returns the list of medicines to be taken in the provided date
     */
    private fun getDateMedicines(list: List<Medicine>): List<Medicine> {
        Timber.d("provide date ${viewModel.providedDate}")
        val tempList: MutableList<Medicine> = list.toMutableList()
        val filteredList =
            tempList.filter { it.startDate.setAsTodayDate().dayOfMonth == Date().dayOfMonth }
        val medicineTakesList: MutableList<Medicine> = mutableListOf()
        filteredList.forEach { medicine ->
            medicine.startDate.setAsProvidedDate(viewModel.providedDate)
                .requireFutureDates(medicine.repeatInterval.toInt(), SpanType.DAY).forEach {

                    if (!medicine.status && medicineTakesList.find { m -> m.name == medicine.name } == null && medicine.startDate.dayOfMonth == viewModel.providedDate.dayOfMonth) {
                        medicineTakesList.add(
                            medicine)
                    }
                    if (it.dayOfMonth == viewModel.providedDate.dayOfMonth && it.after(
                            medicine.startDate) && medicine.status
                    ) {
                        medicineTakesList.add(
                            medicine.copy(
                                startDate = it))
                    }
                }
        }
        medicineTakesList.sortBy { it.startDate }
        return medicineTakesList
    }
}
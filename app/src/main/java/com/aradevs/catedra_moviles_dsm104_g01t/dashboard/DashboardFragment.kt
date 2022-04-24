package com.aradevs.catedra_moviles_dsm104_g01t.dashboard

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.catedra_moviles_dsm104_g01t.*
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.adapters.MedicineListAdapter
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.FragmentDashboardBinding
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.c3rberuss.androidutils.*
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private val binding: FragmentDashboardBinding by viewBinding()
    private val viewModel: DashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMedicineStatus()
        viewModel.getMedicines()
    }

    private fun observeMedicineStatus() {
        viewModel.medicineStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> Snackbar.make(binding.root,
                    getString(R.string.error_obtaining_info),
                    LENGTH_SHORT).show()
                is Status.Success -> {
                    setupUI()
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    private fun setupUI() {
        setupHeader()
        binding.timeSpanSelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //do nothing
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    setUpRecyclerViewData()
                }
            }

        setUpRecyclerViewData()
    }

    private fun setupHeader() {
        try {
            val medicine =
                viewModel.medicineList.first { medicine ->
                    medicine.startDate.setAsTodayDate()
                        .isWithinNextHour(medicine.repeatInterval.toInt())
                }
            binding.title.text = medicine.name
            binding.time.text =
                medicine.startDate.setAsTodayDate().getClosestDate(medicine.repeatInterval.toInt())
                    .toDayMonthYearHour()
            binding.description.text = getString(R.string.current_medicine_header_description)
            binding.headerCard.setCardBackgroundColor(requireContext().getColor(R.color.pending_orange))
            binding.topView.setBackgroundColor(requireContext().getColor(R.color.pending_orange))
        } catch (e: Exception) {
            binding.title.text = "Nothing to see"
            binding.time.text = getString(R.string.empty_string)
            binding.description.text = getString(R.string.current_medicine_empty_header_description)
            binding.headerCard.setCardBackgroundColor(requireContext().getColor(R.color.nothing_pending_green))
            binding.topView.setBackgroundColor(requireContext().getColor(R.color.nothing_pending_green))
        }

    }

    private fun setUpRecyclerViewData() {
        val listWithFilters = viewModel.medicineList.withFilters()
        if (listWithFilters.isNullOrEmpty()) {
            binding.emptyMedicines.root.showThisAndHide(binding.medicineList)
            return
        }
        binding.medicineList.showThisAndHide(binding.emptyMedicines.root)
        recyclerViewLayoutSelector()
        binding.medicineList.adapter = MedicineListAdapter(false).apply {
            submitList(listWithFilters)
        }
    }

    private fun List<Medicine>.withFilters(): List<Medicine> {
        Timber.d("data filtered")
        return when (binding.timeSpanSelector.selectedItemPosition) {
            0 -> {
                val filteredList =
                    this.filter { it.startDate.setAsTodayDate().dayOfMonth == Date().dayOfMonth }
                val medicineTakesList: MutableList<Medicine> = mutableListOf()
                filteredList.forEach { medicine ->
                    medicine.startDate.setAsTodayDate()
                        .requireDatesOfNext24H(medicine.repeatInterval.toInt()).forEach {
                            if (it.dayOfMonth == Date().dayOfMonth && it.after(Date())) medicineTakesList.add(
                                medicine.copy(
                                    startDate = it))
                        }
                }
                medicineTakesList.sortBy { it.startDate }
                return medicineTakesList
            }
            1 -> {
                val medicineTakesList: MutableList<Medicine> = mutableListOf()
                this.forEach { medicine ->
                    medicine.startDate.setAsTodayDate()
                        .requireDatesOfNextWeek(medicine.repeatInterval.toInt()).forEach {
                            if (it.after(Date())) medicineTakesList.add(medicine.copy(startDate = it))
                        }
                }
                medicineTakesList.sortBy { it.startDate }
                return medicineTakesList
            }
            else -> this
        }
    }

    private fun recyclerViewLayoutSelector() {
        when (binding.timeSpanSelector.selectedItemPosition) {
            0 -> binding.medicineList.layoutManager = linearLayoutManager()
            else -> binding.medicineList.layoutManager = gridLayoutManager(R.integer.two_columns)
        }
    }
}
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
import com.aradevs.catedra_moviles_dsm104_g01t.dashboard.dialogs.AddMedicineDialog
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.FragmentDashboardBinding
import com.aradevs.domain.Medicine
import com.aradevs.domain.SpanType
import com.aradevs.domain.coroutines.Status
import com.c3rberuss.androidutils.*
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private val binding: FragmentDashboardBinding by viewBinding()
    private val viewModel: DashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMedicineStatus()
        viewModel.getMedicines()

        binding.addMedicine.setOnClickListener {
            AddMedicineDialog.newInstance { onMedicineAdd(it) }
                .show(childFragmentManager, "add_medicine")
        }
        binding.refresher.setOnRefreshListener {
            viewModel.getMedicines()
        }
    }

    private fun onMedicineAdd(medicine: Medicine) {
        viewModel.saveMedicine(medicine)
    }

    private fun observeMedicineStatus() {
        viewModel.medicineStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    Snackbar.make(binding.root,
                        getString(R.string.error_obtaining_info),
                        LENGTH_SHORT).show()
                    binding.refresher.isRefreshing = false
                }
                is Status.Success -> {
                    binding.refresher.isRefreshing = false
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
            with(binding) {
                title.text = medicine.name
                time.text =
                    medicine.startDate.setAsTodayDate()
                        .getClosestDate(medicine.repeatInterval.toInt())
                        ?.toDayMonthYearHour()
                description.text = getString(R.string.current_medicine_header_description)
                headerCard.setCardBackgroundColor(requireContext().getColor(R.color.pending_orange))
                topView.setBackgroundColor(requireContext().getColor(R.color.pending_orange))
            }
        } catch (e: Exception) {
            with(binding) {
                title.text = getString(R.string.all_good)
                time.text = getString(R.string.empty_string)
                description.text = getString(R.string.current_medicine_empty_header_description)
                headerCard.setCardBackgroundColor(requireContext().getColor(R.color.nothing_pending_green))
                topView.setBackgroundColor(requireContext().getColor(R.color.nothing_pending_green))
            }
        }

    }

    private fun setUpRecyclerViewData() {
        val listWithFilters = withFilters(viewModel.medicineList)
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

    private fun withFilters(list: List<Medicine>): List<Medicine> {
        val tempList: MutableList<Medicine> = list.toMutableList()
        return when (binding.timeSpanSelector.selectedItemPosition) {
            0 -> {
                val filteredList =
                    tempList.filter { it.startDate.setAsTodayDate().dayOfMonth == Date().dayOfMonth }
                val medicineTakesList: MutableList<Medicine> = mutableListOf()
                filteredList.forEach { medicine ->
                    medicine.startDate.setAsTodayDate()
                        .requireFutureDates(medicine.repeatInterval.toInt(), SpanType.DAY).forEach {
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
                tempList.forEach { medicine ->
                    medicine.startDate.setAsTodayDate()
                        .requireFutureDates(medicine.repeatInterval.toInt(), SpanType.WEEK)
                        .forEach {
                            if (it.after(Date())) medicineTakesList.add(medicine.copy(startDate = it))
                        }
                }
                medicineTakesList.sortBy { it.startDate }
                return medicineTakesList
            }
            else -> tempList
        }
    }

    private fun recyclerViewLayoutSelector() {
        when (binding.timeSpanSelector.selectedItemPosition) {
            0 -> binding.medicineList.layoutManager = linearLayoutManager()
            else -> binding.medicineList.layoutManager = gridLayoutManager(R.integer.two_columns)
        }
    }
}
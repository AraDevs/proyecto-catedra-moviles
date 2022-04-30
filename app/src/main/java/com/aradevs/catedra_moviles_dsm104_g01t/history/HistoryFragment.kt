package com.aradevs.catedra_moviles_dsm104_g01t.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.adapters.MedicineListAdapter
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.FragmentHistoryBinding
import com.aradevs.domain.Medicine
import com.aradevs.domain.coroutines.Status
import com.c3rberuss.androidutils.showThisAndHide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding: FragmentHistoryBinding by viewBinding()
    private val viewModel: HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMedicineStatus()
        viewModel.getMedicines()
    }

    private fun observeMedicineStatus() {
        viewModel.medicineStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.Error -> {
                    Snackbar.make(binding.root,
                        getString(R.string.error_obtaining_info),
                        BaseTransientBottomBar.LENGTH_SHORT).show()
                }
                is Status.Success -> {
                    setupUI(status.data)
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    private fun setupUI(items: List<Medicine>) {
        if (items.isNullOrEmpty()) {
            binding.emptyMedicines.root.showThisAndHide(binding.medicineList)
            return
        }
        binding.medicineList.showThisAndHide(binding.emptyMedicines.root)
        binding.medicineList.adapter = MedicineListAdapter(true) {
            onMedicineDeleteTapped(it)
        }.apply {
            submitList(items)
        }
    }

    private fun onMedicineDeleteTapped(medicine: Medicine) {
        viewModel.deleteMedicine(medicine.id)
    }
}
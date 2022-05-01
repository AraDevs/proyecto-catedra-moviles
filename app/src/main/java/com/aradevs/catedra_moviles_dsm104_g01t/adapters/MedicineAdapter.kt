package com.aradevs.catedra_moviles_dsm104_g01t.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.MedicineItemBinding
import com.aradevs.domain.Medicine
import com.aradevs.domain.RenderLocation
import com.c3rberuss.androidutils.toDayMonthYearHour
import timber.log.Timber

class MedicineListAdapter(
    private val needsFullInfo: Boolean,
    private val renderLocation: RenderLocation,
    private val onDeleteTapped: (Medicine) -> Unit,
) :
    ListAdapter<Medicine, MedicineListAdapter.ProductViewHolder>(diffUtils) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return MedicineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            ProductViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: MedicineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(medicine: Medicine) {
            with(binding) {
                title.text = medicine.name
                description.isVisible = needsFullInfo
                description.text = medicine.doctorName
                dateTime.text = medicine.startDate.toDayMonthYearHour()
                colorIndicator.setBackgroundColor(Color.parseColor(medicine.color))
                root.setOnClickListener {
                    deleteMedicine.isVisible = !deleteMedicine.isVisible
                }
                deleteMedicine.text = selectDeleteLabel(root.context)
                deleteMedicine.setOnClickListener {
                    Timber.d("TBD ${medicine.name}")
                    onDeleteTapped(medicine)
                }
            }
        }
    }

    private fun selectDeleteLabel(context: Context): String {
        return when (renderLocation) {
            RenderLocation.DASHBOARD -> context.getString(R.string.medicine_no_longer_in_use)
            RenderLocation.CALENDAR -> context.getString(R.string.hide_medicine_from_list)
            RenderLocation.HISTORY -> context.getString(R.string.delete_medicine)
        }
    }
}

private val diffUtils = object : DiffUtil.ItemCallback<Medicine>() {
    override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean {
        return oldItem == newItem
    }

}
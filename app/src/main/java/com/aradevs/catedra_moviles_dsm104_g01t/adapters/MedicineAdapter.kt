package com.aradevs.catedra_moviles_dsm104_g01t.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.MedicineItemBinding
import com.aradevs.domain.Medicine
import com.c3rberuss.androidutils.toDayMonthYearHour

class MedicineListAdapter(private val needsFullInfo: Boolean) :
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
                dateTime.text = medicine.startDate.toDayMonthYearHour()
            }
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
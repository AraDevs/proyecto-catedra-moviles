package com.aradevs.catedra_moviles_dsm104_g01t.dashboard.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.databinding.AddMedicineLayoutBinding
import com.aradevs.domain.Medicine
import com.c3rberuss.androidutils.base_views.BaseDialogFragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * [AddMedicineDialog] Dialog that should be displayed when [Medicine] data is requested
 */
class AddMedicineDialog : BaseDialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: AddMedicineLayoutBinding
    private lateinit var onTap: (Medicine) -> Unit
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var dateDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private var colorHex: String? = null

    @SuppressLint("SimpleDateFormat")
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AddMedicineLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupCalendar()
            setupTime()

            save.setOnClickListener {
                saveMedicine()
            }
            cancel.setOnClickListener {
                dismiss()
            }

            initDate.setOnClickListener {
                dateDialog.show()
            }

            colorPicker.setOnClickListener {
                ColorPickerDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.select_a_color))
                    .setColorShape(ColorShape.SQAURE)
                    .setColorListener { color, colorHexValue ->
                        colorHex = colorHexValue
                        Timber.d(colorHex)
                        colorPicker.setBackgroundColor(color)
                    }
                    .show()
            }
        }
    }

    /**
     * Contains the "save" actions
     */
    private fun saveMedicine() {
        try {
            if (validator()) {
                with(binding) {
                    val initDate: Date = format.parse(initDate.text.toString())!!
                    val numericInterval = repeatInterval.text.toString().toInt()
                    if (numericInterval <= 0 || numericInterval > 24) {
                        Snackbar.make(root,
                            getString(R.string.valid_hour_interval),
                            LENGTH_SHORT).show()
                        return
                    }
                    onTap(Medicine(0,
                        name.text.toString(),
                        doctorName.text.toString(),
                        initDate,
                        numericInterval.toString(),
                        colorHex ?: "#346DC3", true, requiresNotification.isChecked))
                }
                dismiss()
            } else {
                Snackbar.make(binding.root,
                    getString(R.string.check_error_add_medicine),
                    LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Snackbar.make(binding.root,
                getString(R.string.validation_error_add_medicine),
                LENGTH_SHORT).show()
        }
    }

    /**
     * Validates if the required fields are not empty
     */
    private fun validator(): Boolean {
        with(binding) {
            return when {
                name.text.trim().isEmpty() -> false
                doctorName.text.trim().isEmpty() -> false
                repeatInterval.text.trim().isEmpty() -> false
                initDate.text.trim().isEmpty() -> false
                colorHex.isNullOrEmpty() -> false
                else -> true
            }
        }

    }

    /**
     * Setting up calendar dialog
     */
    private fun setupCalendar() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONDAY) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dateDialog = DatePickerDialog(requireContext(), this, year, month - 1, day)
        dateDialog.datePicker.maxDate = Date().time
    }

    /**
     * Setting up the time picker dialog
     */
    private fun setupTime() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    /**
     * Listening to the onDateSet method of [DatePickerDialog.setOnDateSetListener] to set the selected date
     * into the specified EditText, this method will inflate the [TimePickerDialog] afterwards
     */
    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val currentMonth = month + 1
        binding.initDate.setText("$day-$currentMonth-$year")
        timePickerDialog.show()
    }

    /**
     * Listening to the onTimeSet method of [TimePickerDialog.onTimeChanged] to set the selected time into
     * the specified EditText
     */
    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {

        when (val currentMinute = if (minute < 10) "0$minute" else minute) {
            0 -> binding.initDate.setText("${binding.initDate.text} $hour:00")

            else -> binding.initDate.setText("${binding.initDate.text} $hour:$currentMinute")
        }

    }

    /**
     * Companion object that receives the onTap method to be executed on [binding] save tap
     */
    companion object {
        fun newInstance(onTap: (Medicine) -> Unit): AddMedicineDialog {
            return AddMedicineDialog().apply {
                this.onTap = onTap
            }
        }
    }
}
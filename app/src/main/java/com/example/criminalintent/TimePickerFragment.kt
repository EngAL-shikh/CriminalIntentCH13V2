package com.example.criminalintent
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.time.Year
import java.util.*
private const val ARG_Time = "time"
class TimePickerFragment:DialogFragment() {

    // طريقة الكتاب
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//
//
//
//
//
//
//      var calendar=Calendar.getInstance()
//
//
//
//         var hour= calendar.get(Calendar.HOUR)
//         var minute=  calendar.get(Calendar.MINUTE)
//
//
//        return  TimePickerDialog(requireContext(), null,  hour, minute, true)
//    }

    companion object {
        fun newInstance(time: Time): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_Time, time)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
    interface Callbacks {

        fun onDateSelected(time: Time)

    }
}
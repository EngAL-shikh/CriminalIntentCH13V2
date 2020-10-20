package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.Year
import java.util.*
private const val ARG_DATE = "date"
class DatePickerFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate : Date = GregorianCalendar(year, month, day).time
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDate)
            }
        }


      var date= arguments?.getSerializable(ARG_DATE) as Date


        //get date from calendar
        var calendar=Calendar.getInstance()
        calendar.time=date
        
        var year= calendar.get(Calendar.YEAR)
        var month=  calendar.get(Calendar.MONTH)
        var day= calendar.get(Calendar.DAY_OF_WEEK)

        //return date
        return  DatePickerDialog(requireContext(),dateListener,year,month,day)
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
    interface Callbacks {

        fun onDateSelected(date: Date)

    }
}
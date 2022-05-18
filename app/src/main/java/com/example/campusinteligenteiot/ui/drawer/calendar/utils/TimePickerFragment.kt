package com.example.campusinteligenteiot.ui.drawer.calendar.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(val listener: (String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val dialog = TimePickerDialog(activity as Context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, hour, minute, true)
        dialog.setTitle("Escoge una hora:")
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener("$hourOfDay:$minute")
    }
}
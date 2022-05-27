package com.example.newtrainerapp.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.newtrainerapp.databinding.DialogBinding

class AddStudentDialog(context: Context) : AlertDialog(context) {
    var binding: DialogBinding = DialogBinding.inflate(layoutInflater)
    private var addListener: ((name: String, surname: String, salary: String) -> Unit)? = null

    fun setOnAddListener(f: (name: String, surname: String, salary:String) -> Unit) {
        addListener = f
    }

    init {
        setTitle("Add Student.")

        setButton(BUTTON_POSITIVE, "Add", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                addListener?.invoke(
                    binding.name.text.toString(),
                    binding.surname.text.toString(),
                    binding.salary.text.toString()
                )
                dismiss()
            }
        })

        setButton(BUTTON_NEGATIVE,"No", object :DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                dismiss()
            }
        })

        setView(binding.root)
    }
}
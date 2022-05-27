package com.example.newtrainerapp.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.newtrainerapp.databinding.DialogBinding

class EditStudentDialog(context: Context,name: String, surname: String, salary: String) : AlertDialog(context) {
    var binding: DialogBinding = DialogBinding.inflate(layoutInflater)
    private var addListener: ((name: String, surname: String, salary: String) -> Unit)? = null

    fun setOnAddListener(f: (name: String, surname: String, salary: String) -> Unit) {
        addListener = f
    }

    init {
        binding.name.setText(name)
        binding.surname.setText(surname)
        binding.salary.setText(salary)

        setTitle("Edit Student $name.")

        setButton(BUTTON_POSITIVE, "Edit", object : DialogInterface.OnClickListener {
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